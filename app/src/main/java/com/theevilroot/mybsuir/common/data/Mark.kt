package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class Mark(
        val subject: String,
        @SerializedName("formOfControl")
        val form: String,
        val fullSubject: String,
        val hours: String,
        val mark: String,
        val date: String,
        val teacher: String,
        val commonMark: Double,
        val commonRetakes: Double,
        val retakesCount: Int
)