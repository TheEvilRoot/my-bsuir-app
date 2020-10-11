package com.theevilroot.mybsuir.common.data

data class LoginResponse(
    val loggedIn: Boolean,
    val message: String
)

data class LoginResult(
    val response: LoginResponse,
    val message: String,
    val cookie: String? = null
)
