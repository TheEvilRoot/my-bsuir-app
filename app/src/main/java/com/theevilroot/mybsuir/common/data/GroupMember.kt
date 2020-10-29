package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

open class GroupMember(
        val position: String,
        @SerializedName("fio")
        val fullName: String,
        val phone: String,
        val email: String
)

class SpecialGroupMember(
        position: String,
        fullName: String,
        phone: String,
        email: String,
        val photoUrl: String?
): GroupMember(position, fullName, phone, email)