package com.theevilroot.mybsuir.common.data

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class MarkBook(
        val number: String,
        val averageMark: Float,
        @SerializedName("markPages")
        private val semesters: Map<String, SemesterData>
) {
    val semestersList: List<Semester>
        get() = semesters.entries.map { Semester(it.key.toInt(), it.value.averageMark, it.value.marks) }
                .filter { it.marks.isNotEmpty() }

}