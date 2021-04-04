package com.theevilroot.mybsuir.common

import android.app.Application
import android.os.Build
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.encryption.base.IEncryptionLayer
import com.theevilroot.mybsuir.common.encryption.KeyStoreEncryptionLayer
import com.theevilroot.mybsuir.common.encryption.LegacyEncryptionLayer
import com.theevilroot.mybsuir.login.LoginModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    private val apiBaseUrl: String = "https://journal.bsuir.by/api/v1/"

    override val kodein = Kodein {
        import(androidXModule(this@App))

        /* Layer 0 */
        bind<ApiService>() with singleton { createApiService() }
        bind<CredentialsStore>() with singleton { CredentialsStore() }

        /* Layer 1 */
        bind<LoginModel>() with singleton { LoginModel(applicationContext, instance(), createEncryptionStack()) }
        bind<SharedModel>() with singleton { SharedModel(instance(), instance(), this@App) }

        /* Layout 2 */
        bind<CacheController>() with singleton { CacheController(instance(), instance()) }
    }

    private fun createHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

    private fun createApiService(): ApiService =
            ServiceFactory.createService(apiBaseUrl, createHttpClient())

    private fun createEncryptionStack(): List<IEncryptionLayer> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listOf(KeyStoreEncryptionLayer())
        } else {
            listOf(LegacyEncryptionLayer(this))
        }
    }

}