package com.theevilroot.mybsuir.auth

import com.theevilroot.mybsuir.auth.data.LoginResult
import com.theevilroot.mybsuir.auth.data.UserCredentials
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthController (
    private val authView: IAuthView,
    private val authModel: AuthModel
) {

    fun login(login: String, password: String) {
        authView.loadingState(true)
        Single.create<LoginResult> {
            try {
                val data = authModel.getLoginResult(UserCredentials(login, password))
                it.onSuccess(data)
            } catch (e: Exception) {
                it.onError(e)
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(::onLoginResult, ::onLoginError)
    }

    private fun onLoginResult(result: LoginResult) {
        if (result.loggedIn) {
            authView.successState()
        } else {
            val message = result.message
            if ("Invalid Credentials" in message) {
                authView.errorState("Неправильный логин или пароль.")
            } else {
                authView.errorState(message)
            }
        }
    }

    private fun onLoginError(e: Throwable) {
        e.printStackTrace()
        authView.errorState(e.localizedMessage ?: "Unexpected error")
    }

}