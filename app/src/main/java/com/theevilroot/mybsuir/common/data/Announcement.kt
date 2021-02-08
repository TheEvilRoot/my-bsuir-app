package com.theevilroot.mybsuir.common.data

data class Announcement(
        val name: String,
        val date: String,
        val content: String,
        val photoUrl: String? = "https://iis.bsuir.by/assets/default-photo.gif",
)