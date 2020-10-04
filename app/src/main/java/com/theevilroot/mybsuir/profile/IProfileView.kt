package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.profile.data.ProfileInfo

interface IProfileView {

    fun loadingState(shown: Boolean = true)

    fun profileState(profile: ProfileInfo)

    fun errorState(message: String)

    fun requireAuth()

}