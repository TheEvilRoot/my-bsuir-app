package com.theevilroot.mybsuir.common

import com.theevilroot.mybsuir.auth.data.LoginResult
import com.theevilroot.mybsuir.auth.data.UserCredentials
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    fun login(@Body credentials: UserCredentials): Call<LoginResult>

    @GET("portal/personalCV")
    fun getProfileInfo(@Header("Cookie") cookie: String): Call<ProfileInfo>

}