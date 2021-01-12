package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class Employee (
        val id: Int,
        val firstName: String,
        val lastName: String,
        val middleName: String,
        @SerializedName("fio")
        val fullName: String,
        val academicDepartment: String,
        val rank: Rank
) {

    data class Rank(val name: String, val price: Float)

}