package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class PersonalInformation(
    val name: String,
    val group: String,
    @SerializedName("studentRecordBookNumber")
    val recordBookNumber: String,
    val email: String,
    val phone: String
)