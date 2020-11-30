package com.theevilroot.mybsuir.group.data

import com.theevilroot.mybsuir.common.data.GroupMember

data class Group (
        val number: String,
        val supervisor: GroupMember?,
        val leader: GroupMember?,
        val students: List<GroupMember>
)