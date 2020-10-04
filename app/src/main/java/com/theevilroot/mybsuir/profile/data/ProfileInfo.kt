package com.theevilroot.mybsuir.profile.data

data class Skill (
    val id: Int,
    val name: String
)

data class Reference (
    val id: String,
    val name: String,
    val reference: String
)

data class ProfileInfo(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String,
    val photoUrl: String?,
    val summary: String?,
    val rating: Int,
    val showRating: Boolean,
    val published: Boolean,
    val searchJob: Boolean,
    val faculty: String,
    val cource: Int,
    val officeEmail: String,
    val officePassword: String,
    val speciality: String,
    val studentGroup: String,
    val skills: List<Skill>,
    val references: List<Reference>
)