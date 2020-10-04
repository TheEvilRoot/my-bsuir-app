package com.theevilroot.mybsuir.auth

interface IAuthView {

    fun loadingState(shown: Boolean)

    fun errorState(message: String)

    fun successState()

}