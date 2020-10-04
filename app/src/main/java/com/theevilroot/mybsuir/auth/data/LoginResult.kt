package com.theevilroot.mybsuir.auth.data

data class LoginResult(
    val loggedIn: Boolean,
    val username: String,
    val fio: String,
    val protoLink: String,
    val message: String
)