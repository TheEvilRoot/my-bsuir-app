package com.theevilroot.mybsuir.profile.data

import com.theevilroot.mybsuir.common.data.PersonalCV
import com.theevilroot.mybsuir.common.data.PersonalInformation
import com.theevilroot.mybsuir.common.data.Reference
import com.theevilroot.mybsuir.common.data.Skill

data class ProfileInfo(
    val fullName: String,
    val shortName: String,
    val birthDate: String,
    val facultyString: String,
    val rate: Int,
    val photoUrl: String,
    val skills: List<Skill>,
    val summary: String?,
    val references: List<Reference>,
    val personalInformation: PersonalInformation,
    val personalCV: PersonalCV
)