package com.theevilroot.mybsuir.auth

import android.content.Context
import com.theevilroot.mybsuir.api.MutableCompositeDataSource
import io.reactivex.rxjava3.core.Observable
import java.io.File

class AuthProvider (context: Context) {

    private val applicationContext = context.applicationContext

    class NoAuthDataException: Exception()

    val dataSource = MutableCompositeDataSource<String>({
        Observable.create<String> { it.onError(NoAuthDataException()); it.onComplete() }
    }, {
         Observable.create<String> {
             getFromDisk()?.let { c -> it.onNext(c) }
             it.onComplete()
         }
    }, ::writeOnDisk, ::clearCache)

    fun setCookie(cookie: String) { dataSource.setMemoryData(cookie) }

    private fun getFromDisk(): String? {
        val file = File(applicationContext.filesDir, "cookie")
        if (!file.exists()) return null

        val data = file.readText()
        val magic = "##MAGIC;"
        if (!data.startsWith(magic)) return null

        return data.removePrefix(magic)
    }

    private fun writeOnDisk(cookie: String) {
        val file = File(applicationContext.filesDir, "cookie")

        val magic = "##MAGIC;"
        val data = "$magic$cookie"
        file.writeText(data)
    }

    private fun clearCache() {
        val file = File(applicationContext.filesDir, "cookie")
        if (file.exists()) {
            file.delete()
        }
    }

}