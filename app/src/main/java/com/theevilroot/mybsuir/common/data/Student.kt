package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class Student(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val middleName: String,
        @SerializedName("fio")
        val fullName: String,
        val studentGroup: String,
)