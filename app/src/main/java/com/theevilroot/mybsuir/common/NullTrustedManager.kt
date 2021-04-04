package com.theevilroot.mybsuir.common

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object NullTrustedManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) { }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) { }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return emptyArray()
    }

    fun OkHttpClient.Builder.initNullTrustManager(): OkHttpClient.Builder {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(NullTrustedManager), SecureRandom())
        sslSocketFactory(sslContext.socketFactory, NullTrustedManager)
        hostnameVerifier { _, _ -> true }
        return this
    }

}