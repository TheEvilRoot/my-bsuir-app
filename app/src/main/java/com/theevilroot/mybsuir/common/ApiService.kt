package com.theevilroot.mybsuir.common

import com.theevilroot.mybsuir.common.data.*
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

    @GET("portal/personalCV")
    fun personalCV(@Header("Cookie") token: String): Call<PersonalCV>

    @GET("portal/groupInfo")
    fun groupInfo(@Header("Cookie") token: String): Call<GroupInfo>

    @GET("portal/markbook")
    fun markBook(@Header("Cookie") token: String): Call<MarkBook>

}