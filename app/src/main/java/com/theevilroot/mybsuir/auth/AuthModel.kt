package com.theevilroot.mybsuir.auth

import com.theevilroot.mybsuir.auth.data.LoginResult
import com.theevilroot.mybsuir.auth.data.UserCredentials
import com.theevilroot.mybsuir.common.ApiService

class AuthModel (
    val apiService: ApiService,
    val authProvider: AuthProvider
) {

    fun getLoginResult(userCredentials: UserCredentials): LoginResult {
        val call = apiService.login(userCredentials).execute()
        if (call.isSuccessful) {
            val body = call.body() ?: throw Exception("Body is null")
            val cookie = call.headers()["Set-Cookie"] ?: return body
            authProvider.setCookie(cookie)
            return body
        } else {
            throw Exception("Login status: ${call.code()}")
        }
    }

}