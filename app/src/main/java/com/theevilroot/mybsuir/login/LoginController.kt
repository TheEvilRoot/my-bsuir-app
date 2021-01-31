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

    fun login(username: String, password: String, rememberCredentials: Boolean): Completable =
        Completable.create {
            val result = model.login(username, password, rememberCredentials, rememberCredentials)
                ?: return@create it.onError(InternalException("Ошибка авторизации: сервер временно недоступен"))
            if (result.cookie == null)
                return@create it.onError(InternalException(result.message))
            store.updateToken(result.cookie)
            it.onComplete()
        }.subscribeOn(Schedulers.io())

    fun logout(): Completable =
        Completable.create {
            model.logout()
            it.onComplete()
        }.subscribeOn(Schedulers.io())

}