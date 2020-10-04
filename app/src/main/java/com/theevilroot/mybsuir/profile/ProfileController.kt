package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.auth.AuthProvider
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single

class ProfileController (
    private val profileView: IProfileView,
    private val profileModel: ProfileModel
){

    fun updateProfileInfo() {
        profileView.loadingState(true)
        profileModel.profileDataSource
            .getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onProfileUpdate, ::onError)
    }

    private fun onProfileUpdate(profile: ProfileInfo) {
        profileView.loadingState(false)
        profileView.profileState(profile)
    }

    private fun onError(error: Throwable) {
        profileView.loadingState(false)
        when (error) {
            is NoSuchElementException -> profileView.errorState("Unable to load data from server")
            is AuthProvider.NoAuthDataException -> profileView.requireAuth()
            else -> profileView.errorState(error.localizedMessage ?: "Unexpected error")
        }
    }

}