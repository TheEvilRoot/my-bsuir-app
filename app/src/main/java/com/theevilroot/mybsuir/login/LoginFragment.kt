package com.theevilroot.mybsuir.login

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.api.views.BaseFragment
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.asVisibility
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.f_login.view.*
import org.kodein.di.generic.instance
import java.net.UnknownHostException

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
class LoginFragment : BaseFragment<LoginFragment.LoginViewState>(R.layout.f_login) {

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
    private val cacheController: CacheController by instance()

    private val controller: LoginController by lazy { LoginController(model, store) }

    override fun View.onView() {
        applyState(LoginViewState.LoginLoadingState)

        cacheController.getCachedCredentials().observeOn(AndroidSchedulers.mainThread())
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
                applyState(LoginViewState.LoginFailedState(when (it) {
                    is InternalException -> it.msg
                    is UnknownHostException ->"Похоже вы не подключены к интернету.\n" +
                            "Включите WiFi или мобильную передачу данных и попробуйте снова :)"
                    else -> "Произошла непредвиденная ошибка\n" +
                            "${it.javaClass.simpleName}:\n ${it.localizedMessage}"
                }))
            }
    }

    private fun setLoginSucceed() {
        val controller = findNavController()
        controller.popBackStack()
        if (controller.currentBackStackEntry == null) {
            controller.navigate(R.id.fragment_profile)
        }
    }

    override fun View.applyState(newState: LoginViewState) = with(newState) {
        login_content.visibility = newState.contentVisibility.asVisibility()
        login_loading.visibility = newState.progressVisibility.asVisibility()
        login_error.visibility = newState.errorVisibility.asVisibility()

        if (this is LoginViewState.LoginFailedState)
            login_error.text = errorMessage

        if (this is LoginViewState.LoginLoadingState)
            hideKeyboard()
    }

    private fun hideKeyboard() {
        activity?.let { activity ->
            val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            activity.currentFocus?.let {
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }

    }
}