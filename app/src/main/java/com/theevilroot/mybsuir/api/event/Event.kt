package com.theevilroot.mybsuir.api.event

sealed class Event (val sender: String) {
    class LoginInitiated (sender: String) : Event(sender)
    class LoginSucceed (sender: String) : Event(sender)
}