package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class ScheduleEntry (
    @SerializedName("weekNumber")
    val weekNumbers: List<Int>,
    @SerializedName("numSubgroup")
    val subgroup: Int,
    val auditory: List<String>,
    @SerializedName("lessonTime")
    val timeInterval: String,
    val subject: String,
    val note: String?,
    @SerializedName("lessonType")
    val type: ScheduleType,
    val employee: List<ScheduleEmployee>,
    @SerializedName("startLessonTime")
    val startTime: String,
    @SerializedName("endLessonTime")
    val endTime: String
)