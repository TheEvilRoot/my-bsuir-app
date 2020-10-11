package com.theevilroot.mybsuir.login

import android.view.View
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.data.InternalException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.f_login.view.*
import org.kodein.di.generic.instance

/**
 * View states:
 *  - Loading:
 *    Content : gone
 *    Error   : gone
 *    Progress: shown
 *
 *  - Idle:
 *    Content : shown
 *    Error   : gone
 *    Progress: gone
 *
 *  - Failed:
 *    Content : shown
 *    Error   : shown
 *    Progress: gone
 */
class LoginFragment : BaseFragment(R.layout.f_login) {

    sealed class LoginViewState {
        abstract val contentVisibility: Boolean
        abstract val errorVisibility: Boolean
        abstract val progressVisibility: Boolean

        object LoginLoadingState: LoginViewState() {
            override val contentVisibility: Boolean = false
            override val errorVisibility: Boolean = false
            override val progressVisibility: Boolean = true
        }
        object LoginIdleState: LoginViewState() {
            override val contentVisibility: Boolean = true
            override val errorVisibility: Boolean = false
            override val progressVisibility: Boolean = false
        }
        class LoginFailedState(val errorMessage: String): LoginViewState() {
            override val contentVisibility: Boolean = true
            override val errorVisibility: Boolean = true
            override val progressVisibility: Boolean = false
        }
    }

    private val model: LoginModel by instance()
    private val store: CredentialsStore by instance()
    private val controller: LoginController by lazy { LoginController(model, store) }

    override fun View.onView() {
        applyState(LoginViewState.LoginLoadingState)

        controller.getCachedCredentials().observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isAuthorized ->
                if (isAuthorized) {
                    return@subscribe setLoginSucceed()
                }
                applyState(LoginViewState.LoginIdleState)
            }) { applyState(LoginViewState.LoginIdleState) }

        login_submit.setOnClickListener { userLogin() }
    }

    private fun View.userLogin() {
        val usernameText = login_username.text?.toString()
        val passwordText = login_password.text?.toString()

        if (usernameText?.isNotBlank() != true) {
            return applyState(LoginViewState.LoginFailedState("Пожалуйста, введите имя пользователя"))
        }
        if (passwordText?.isNotBlank() != true) {
            return applyState(LoginViewState.LoginFailedState("Пожалуйста, введите пароль"))
        }
        if (usernameText.length < 5) {
            return applyState(LoginViewState.LoginFailedState("Имя пользователя не может быть таким коротким"))
        }

        applyState(LoginViewState.LoginLoadingState)
        controller.login(usernameText, passwordText)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setLoginSucceed) {
                if (it is InternalException) {
                    applyState(LoginViewState.LoginFailedState(it.msg))
                } else{
                    applyState(LoginViewState.LoginFailedState("Произошла непредвиденная ошибка\n${it.javaClass.simpleName}:\n ${it.localizedMessage}"))
                }
            }
    }

    private fun setLoginSucceed() {
        view?.applyState(LoginViewState.LoginFailedState("Success"))
    }

    private fun View.applyState(newState: LoginViewState) = with(newState) {
        login_content.visibility = newState.contentVisibility.visibility()
        login_progress.visibility = newState.progressVisibility.visibility()
        login_error.visibility = newState.errorVisibility.visibility()

        if (this is LoginViewState.LoginFailedState)
            login_error.text = errorMessage
    }

    private fun Boolean.visibility() = if (this) View.VISIBLE else View.GONE
}