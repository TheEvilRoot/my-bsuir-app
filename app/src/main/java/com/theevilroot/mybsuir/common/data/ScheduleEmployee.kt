package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class ScheduleEmployee (
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val photoLink: String?,
    val calendarId: String,
    @SerializedName("fio")
    val fullName: String
)