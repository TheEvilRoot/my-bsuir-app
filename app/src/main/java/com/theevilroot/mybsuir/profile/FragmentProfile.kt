package com.theevilroot.mybsuir.profile

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.api.BaseFragment
import com.theevilroot.mybsuir.api.event.Event
import com.theevilroot.mybsuir.api.groupOf
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import kotlinx.android.synthetic.main.f_profile.*
import kotlinx.android.synthetic.main.f_profile.profile_log_out_button
import kotlinx.android.synthetic.main.f_profile.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.jar.Manifest

class FragmentProfile : BaseFragment(R.layout.f_profile), IProfileView, KodeinAware {

    override val kodein: Kodein by kodein()

    private val profileModel: ProfileModel by instance()
    private val controller: ProfileController by lazy {
        ProfileController(this, profileModel)
    }

    private val profileInfoGroup by lazy {
        groupOf(profile_image_view, profile_course_view, profile_id_view, profile_name_view, profile_group, profile_group_label, profile_log_out_button)
    }

    private val profileProgressGroup by lazy {
        groupOf(profile_progress_view)
    }

    override fun View.onView() {
        profile_log_out_button.setOnClickListener { controller.logout() }
        controller.updateProfileInfo()
        loadingState(true)
    }

    override fun onSystemEvent(event: Event) {
        when (event) {
            is Event.LoginInitiated -> Log.d(this.javaClass.simpleName, "LoginInitiated")
            is Event.LoginSucceed -> controller.updateProfileInfo()
        }
    }

    override fun profileState(profile: ProfileInfo) {
        loadingState(false)
        with(profile) {
            profile_name_view.text = "$lastName $firstName $middleName"
            profile_id_view.text = "N/A"
            profile_group.text = studentGroup
            profile_course_view.text = "$cource курс $faculty\nспециальности $speciality"
            Glide.with(this@FragmentProfile)
                .load(photoUrl ?: "https://iis.bsuir.by/assets/default-photo.gif")
                .centerInside()
                .into(profile_image_view)

        }
    }

    override fun errorState(message: String) {
        loadingState(false)
        view?.let { Toast.makeText(it.context, "Error: $message", Toast.LENGTH_LONG).show() }
    }

    override fun loadingState(shown: Boolean) {
        if (shown) {
            profileProgressGroup.visible()
            profileInfoGroup.invisible()
        } else {
            profileProgressGroup.gone()
            profileInfoGroup.visible()
        }
    }

    override fun requireAuth() {
        findNavController().navigate(R.id.open_login)
    }

    override fun requirePermissions() {
        requestPermissions(arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 6741)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 6741) {
            controller.updateProfileInfo()
        }
    }

}