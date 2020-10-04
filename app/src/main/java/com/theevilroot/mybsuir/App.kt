package com.theevilroot.mybsuir

import android.app.Application
import com.theevilroot.mybsuir.api.event.EventBus
import com.theevilroot.mybsuir.auth.AuthModel
import com.theevilroot.mybsuir.auth.AuthProvider
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.profile.ProfileModel
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application(), KodeinAware {

    private val apiUrl: String = "https://journal.bsuir.by/api/v1/"

    override val kodein: Kodein = Kodein {
        import(androidXModule(this@App))

        bind<EventBus>(tag = "System") with singleton { EventBus() }
        bind<ApiService>() with singleton { createApiService() }
        bind<AuthProvider>() with singleton { AuthProvider() }
        bind<ProfileModel>() with singleton { ProfileModel(instance(), instance()) }
        bind<AuthModel>() with singleton { AuthModel(instance(), instance()) }
    }


    private fun createApiService(): ApiService =
        Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .build())
            .build()
            .create(ApiService::class.java)

}