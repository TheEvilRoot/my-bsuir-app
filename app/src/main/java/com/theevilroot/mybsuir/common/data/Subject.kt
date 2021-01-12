package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class Subject(
        val id: Int,
        val name: String,
        @SerializedName("abbrev")
        val shortName: String,
        @SerializedName("lessonTypeAbbrev")
        val lessonType: String,
        val term: Int,
)
