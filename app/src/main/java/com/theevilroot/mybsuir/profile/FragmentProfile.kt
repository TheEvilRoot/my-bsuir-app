package com.theevilroot.mybsuir.profile

import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.api.BaseFragment
import com.theevilroot.mybsuir.api.event.Event
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import kotlinx.android.synthetic.main.f_profile.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class FragmentProfile : BaseFragment(R.layout.f_profile), IProfileView, KodeinAware {

    override val kodein: Kodein by kodein()

    private val profileModel: ProfileModel by instance()
    private val controller: ProfileController by lazy {
        ProfileController(this, profileModel)
    }

    override fun View.onView() {
        controller.updateProfileInfo()
    }

    override fun onSystemEvent(event: Event) {
        when (event) {
            is Event.LoginInitiated -> Log.d(this.javaClass.simpleName, "LoginInitiated")
            is Event.LoginSucceed -> controller.updateProfileInfo()
        }
    }

    override fun profileState(profile: ProfileInfo) {
        profile_view.text = profile.firstName
    }

    override fun errorState(message: String) {
        profile_view.text = "Error: $message"
    }

    override fun loadingState(shown: Boolean) {
         profile_view.text = if (shown) "Loading..." else ""
    }

    override fun requireAuth() {
        findNavController().navigate(R.id.dialog_login)
    }

}