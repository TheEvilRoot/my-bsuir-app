package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class PersonalCV (
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val photoUrl: String?,
    val showRating: Boolean,
    val published: Boolean,
    val searchJob: Boolean,
    val summary: String?,
    val rating: Float,
    val faculty: String,
    @SerializedName("cource")
    val course: Int,
    val officeEmail: String,
    val officePassword: String,
    val speciality: String,
    val studentGroup: String,
    val birthDate: String,
    val skills: List<Skill>
)