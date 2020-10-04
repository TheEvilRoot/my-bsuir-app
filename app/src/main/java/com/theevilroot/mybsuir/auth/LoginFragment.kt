package com.theevilroot.mybsuir.auth

import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.api.BaseBottomSheetFragment
import com.theevilroot.mybsuir.api.BaseFragment
import com.theevilroot.mybsuir.api.ViewGroup
import com.theevilroot.mybsuir.api.event.Event
import com.theevilroot.mybsuir.api.event.EventBus
import com.theevilroot.mybsuir.api.groupOf
import kotlinx.android.synthetic.main.f_login.*
import kotlinx.android.synthetic.main.f_login.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class LoginFragment : BaseFragment(R.layout.f_login), KodeinAware, IAuthView {

    override val kodein: Kodein by kodein()

    private val authModel: AuthModel by instance()
    private val systemEventBus: EventBus by instance("System")
    private val controller: AuthController by lazy { AuthController(this, authModel) }

    private val controlsGroup by lazy {
        groupOf(login_view, password_view, login_hint_view, login_button, reset_password_button)
    }
    private val progressGroup by lazy {
        groupOf(login_progress_view)
    }

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

        idleState()
    }

    private fun idleState() {
        progressGroup.gone()
        controlsGroup.visible()
        login_error_view.visibility = View.GONE
    }

    override fun errorState(message: String) {
        idleState()
        login_error_view.visibility = View.VISIBLE
        login_error_view.text = message
    }

    override fun loadingState(shown: Boolean) {
        controlsGroup.gone()
        progressGroup.visible()
        login_error_view.visibility = View.GONE
    }

    override fun successState() {
        systemEventBus.publishable()
            .onNext(Event.LoginSucceed("LoginFragment"))
        findNavController().popBackStack()
    }

}