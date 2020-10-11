package com.theevilroot.mybsuir.common

import com.theevilroot.mybsuir.common.data.LoginRequest
import com.theevilroot.mybsuir.common.data.LoginResponse
import com.theevilroot.mybsuir.common.data.LoginResult
import com.theevilroot.mybsuir.common.data.PersonalInformation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("portal/personalInformation")
    fun personalInformation(@Header("Cookie") token: String): Call<PersonalInformation>

}