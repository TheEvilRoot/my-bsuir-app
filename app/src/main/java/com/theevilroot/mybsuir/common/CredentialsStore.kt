package com.theevilroot.mybsuir.common

class CredentialsStore {

    private var token: String? = null

    fun updateToken(t: String) {
        token = t
    }

}