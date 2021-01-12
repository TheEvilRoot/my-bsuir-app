package com.theevilroot.mybsuir.common

import android.app.Application
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.group.GroupModel
import com.theevilroot.mybsuir.login.LoginModel
import com.theevilroot.mybsuir.markbook.MarkBookModel
import com.theevilroot.mybsuir.papers.PapersModel
import com.theevilroot.mybsuir.profile.ProfileModel
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
        bind<ApiService>() with singleton { createApiService() }
        bind<CredentialsStore>() with singleton { CredentialsStore() }

        /* Layer 1 */
        bind<LoginModel>() with singleton { LoginModel(applicationContext, instance()) }
        bind<ProfileModel>() with singleton { ProfileModel(instance(), instance(), this@App) }
        bind<GroupModel>() with singleton { GroupModel(instance(), instance(), this@App) }
        bind<MarkBookModel>() with singleton { MarkBookModel(instance(), instance(), this@App) }
        bind<PapersModel>() with singleton { PapersModel(instance(), instance(), this@App) }

        /* Layout 2 */
        bind<CacheController>() with singleton { CacheController(instance(), instance()) }
        bind<SharedModel>() with singleton { SharedModel(instance(), instance(), instance(), instance()) }
    }

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

    private fun createApiService(): ApiService =
            ServiceFactory.createService(apiBaseUrl, createHttpClient())

}