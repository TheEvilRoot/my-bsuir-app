package com.theevilroot.mybsuir.common.controller

import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.login.LoginModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CacheController (
        val loginModel: LoginModel,
        val store: CredentialsStore
) {

    fun <T> preloadCacheAndCall(data: Single<T>, checkMemory: Boolean = true): Single<T> =
            if (checkMemory && hasCredentials()) {
                data.map { it }
            } else {
                getCachedCredentials()
                        .flatMap { isLoaded ->
                            if (isLoaded)
                                data
                            else Single.create { i -> i.onError(NoCredentialsException()) }
                        }
            }

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
                val cache = loginModel.readCacheFile()
                        ?: return@create it.onSuccess(false)

                if (loginModel.checkUserToken(cache.token)) {
                    store.updateToken(cache.token)
                    return@create it.onSuccess(true)
                }

                val (login, password) = cache.credentials
                        ?: return@create it.onSuccess(false)

                val result = loginModel.login(login, password, saveToken=false, saveCredentials=true)
                if (result?.cookie == null)
                    return@create it.onSuccess(false)

                store.updateToken(result.cookie)
                it.onSuccess(true)
            }.subscribeOn(Schedulers.io())

    fun hasCredentials(): Boolean =
            store.hasToken()
}