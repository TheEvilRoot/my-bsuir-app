package com.theevilroot.mybsuir.common

import com.theevilroot.mybsuir.common.data.*
import retrofit2.Call
import retrofit2.http.*

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

    @GET("portal/certificate")
    fun certificate(@Header("Cookie") token: String): Call<List<Paper>>

    @GET("portal/certificate/places")
    fun places(@Header("Cookie") token: String): Call<List<PaperPlaceCategory>>

    @GET("portal/markSheet")
    fun markSheet(@Header("Cookie") token: String): Call<List<MarkSheet>>

    @GET("portal/announcement")
    fun announcements(@Header("Cookie") token: String): Call<List<Announcement>>

    @GET("https://journal.bsuir.by/api/v1/portal/schedule/gradebook?date=Fri%20Feb%2012%202021")
    fun daySchedule(@Header("Cookie") token: String): Call<DaySchedule>
}