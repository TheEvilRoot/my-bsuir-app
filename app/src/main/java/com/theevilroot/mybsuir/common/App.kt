package com.theevilroot.mybsuir.common

import android.app.Application
import com.theevilroot.mybsuir.login.LoginModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application(), KodeinAware {

    private val apiBaseUrl: String = "https://journal.bsuir.by/api/v1/"

    override val kodein = Kodein {
        import(androidXModule(this@App))

        /* Layer 0 */
        bind<ApiService>() with singleton { createService<ApiService>(apiBaseUrl) }
        bind<CredentialsStore>() with singleton { CredentialsStore() }

        /* Layer 1 */
        bind<LoginModel>() with singleton { LoginModel(applicationContext, instance()) }
    }

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

    private inline fun <reified T> createService(baseUrl: String): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()
            .create(T::class.java)

}