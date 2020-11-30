package com.theevilroot.mybsuir.common.data

import com.google.gson.annotations.SerializedName

data class GroupInfo(
        val numberGroup: String,
        @SerializedName("groupInfoStudentDto")
        val members: List<GroupMember>
)