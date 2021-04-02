package com.theevilroot.mybsuir.common

import com.theevilroot.mybsuir.common.data.*
import okhttp3.ResponseBody
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

    @GET("portal/schedule/gradebook")
    fun daySchedule(@Header("Cookie") token: String, @Query("date") date: String): Call<DaySchedule>

    @GET("portal/skills")
    fun availableSkills(@Header("Cookie") token: String): Call<List<Skill>>

    @PUT("portal/addSkill")
    fun newSkill(@Header("Cookie") token: String, @Body body: NewSkill): Call<Skill>

    @POST("portal/skill")
    fun addSkill(@Header("Cookie") token: String, @Body skill: Skill): Call<ResponseBody>

    @PUT("portal/summary")
    fun summary(@Header("Cookie") token: String, @Body body: NewSummary): Call<ResponseBody>

}