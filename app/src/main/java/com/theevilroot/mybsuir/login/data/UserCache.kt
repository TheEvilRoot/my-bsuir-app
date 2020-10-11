package com.theevilroot.mybsuir.login.data

data class UserCache(
    val token: String,
    val credentials: Pair<String, String>? = null
)