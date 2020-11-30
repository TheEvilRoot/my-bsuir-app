package com.theevilroot.mybsuir.common

class CredentialsStore {

    private var token: String? = null

    fun getToken(): String? =
        token

    fun updateToken(t: String) {
        token = t
    }

    fun hasToken(): Boolean =
        token != null

}