package com.theevilroot.mybsuir.auth

import com.theevilroot.mybsuir.api.MutableCompositeDataSource
import io.reactivex.rxjava3.core.Observable

class AuthProvider {

    class NoAuthDataException: Exception()

    val dataSource = MutableCompositeDataSource<String> {
        Observable.create<String> { it.onError(NoAuthDataException()) }
    }

    fun setCookie(cookie: String) { dataSource.setMemoryData(cookie) }

}