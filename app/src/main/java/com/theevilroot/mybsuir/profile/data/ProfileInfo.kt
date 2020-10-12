package com.theevilroot.mybsuir.profile.data

import com.theevilroot.mybsuir.common.data.PersonalCV
import com.theevilroot.mybsuir.common.data.PersonalInformation

data class ProfileInfo(
    val fullName: String,
    val shortName: String,
    val birthDate: String,
    val facultyString: String,
    val rate: Int,
    val personalInformation: PersonalInformation,
    val personalCV: PersonalCV
)