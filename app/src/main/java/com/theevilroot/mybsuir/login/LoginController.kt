package com.theevilroot.mybsuir.login

import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.LoginResult
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginController (
    private val model: LoginModel,
    private val store: CredentialsStore
) {

    /**
     * Read file from disk
     * Get token from file
     * Check if token is valid, save token to provider
     * If token is expired, read user/pass from file
     * If present, get token and save to provider. return true
     * If not present, return false
     * If failed to login with credentials, return false
     */
    fun getCachedCredentials(): Single<Boolean> =
        Single.create<Boolean> {
            val cache = model.readCacheFile()
                ?: return@create it.onSuccess(false)

            if (model.checkUserToken(cache.token)) {
                store.updateToken(cache.token)
                return@create it.onSuccess(true)
            }

            val (login, password) = cache.credentials
                ?: return@create it.onSuccess(false)

            val result = model.login(login, password)
            if (result?.cookie == null)
                return@create it.onSuccess(false)

            store.updateToken(result.cookie)
            it.onSuccess(true)
        }.subscribeOn(Schedulers.io())

    fun login(username: String, password: String): Completable =
        Completable.create {
            val result = model.login(username, password)
                ?: return@create it.onError(InternalException("Ошибка авторизации: сервер временно недоступен"))
            if (result.cookie == null)
                return@create it.onError(InternalException(result.message))
            it.onComplete()
        }.subscribeOn(Schedulers.io())


}