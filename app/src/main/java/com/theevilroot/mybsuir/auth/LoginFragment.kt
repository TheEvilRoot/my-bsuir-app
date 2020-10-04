package com.theevilroot.mybsuir.auth

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.api.BaseBottomSheetFragment
import com.theevilroot.mybsuir.api.event.Event
import com.theevilroot.mybsuir.api.event.EventBus
import kotlinx.android.synthetic.main.f_login.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LoginFragment : BaseBottomSheetFragment(R.layout.f_login), KodeinAware, IAuthView {

    override val kodein: Kodein by kodein()

    private val authModel: AuthModel by instance()
    private val systemEventBus: EventBus by instance("System")
    private val controller: AuthController by lazy { AuthController(this, authModel) }

    override fun View.onView() {
        systemEventBus.publishable()
            .onNext(Event.LoginInitiated("LoginFragment"))

        login_button.setOnClickListener {
            val login = login_view.text.toString()
            val password = password_view.text.toString()

            if (login.isNotBlank() && password.isNotBlank()) {
                controller.login(login, password)
            }
        }
    }

    override fun errorState(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE) }
    }

    override fun loadingState(shown: Boolean) { }

    override fun successState() {
        systemEventBus.publishable()
            .onNext(Event.LoginSucceed("LoginFragment"))
        dismiss()
    }

}