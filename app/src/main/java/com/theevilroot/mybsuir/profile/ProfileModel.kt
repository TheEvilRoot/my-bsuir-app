package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.api.CompositeDataSource
import com.theevilroot.mybsuir.auth.AuthProvider
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.profile.data.ProfileInfo

class ProfileModel (
    private val apiService: ApiService,
    private val authProvider: AuthProvider
) {

    val profileDataSource =
        CompositeDataSource<ProfileInfo> {
            authProvider.dataSource.getData().map {
                val call = apiService.getProfileInfo(it).execute()
                if (call.isSuccessful) {
                    call.body() ?: throw Exception("Body is null")
                } else {
                    throw Exception("Unexpected network error : ${call.code()} ${call.errorBody()?.string()}")
                }
            }.toObservable()
        }

}