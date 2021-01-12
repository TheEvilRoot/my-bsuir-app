package com.theevilroot.mybsuir.common.data

data class MarkSheet(
        val id: Int,
        val subject: Subject,
        val type: MarkSheetType,
        val employee: Employee,
        val absentDate: String,
        val createDate: String,
        val reason: Int, // TODO: what is that?
        val status: String,
        val student: Student,
        val rejectionReason: String?,
        val totalApproved: Int,
        val retakeCount: Int,
        val term: Int,
)