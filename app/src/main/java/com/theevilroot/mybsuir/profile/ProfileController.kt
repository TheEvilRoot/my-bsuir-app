package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.auth.AuthProvider
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

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

    fun logout() {
        Completable.create {
            profileModel.logout()
            it.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onLogoutSuccess, ::onError)
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
            is SecurityException -> profileView.requirePermissions()
            else -> profileView.errorState(error.localizedMessage ?: "Unexpected error")
        }
    }

    private fun onLogoutSuccess() {
        profileView.requireAuth()
    }

}