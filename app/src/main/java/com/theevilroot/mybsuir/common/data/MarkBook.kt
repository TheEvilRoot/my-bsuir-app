package com.theevilroot.mybsuir.common.data

import com.google.gson.JsonObject

class MarkBook(
        val number: String,
        val averageMark: Float,
        val semesters: Map<String, Semester>
)