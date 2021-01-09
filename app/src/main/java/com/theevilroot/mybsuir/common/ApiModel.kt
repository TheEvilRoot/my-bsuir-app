package com.theevilroot.mybsuir.common

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.ReAuthRequiredException
import retrofit2.Call
import java.net.UnknownHostException

open class  ApiModel(
        val api: ApiService,
        val store: CredentialsStore,
        val applicationContext: Context
) {

    private fun Context.isNetworkAvailable(): Boolean =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
            val networkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.getNetworkInfo(it.activeNetwork)
            } else {
                it.activeNetworkInfo
            }
            networkInfo?.run { isConnectedOrConnecting && isAvailable } == true
        }

    fun <T> apiCall(func: ApiService.(String) -> Call<T>, cachedData: T? = null): T? {
        if (cachedData != null)
            return cachedData
        val token = store.getToken()
                ?: throw NoCredentialsException()
        if (!applicationContext.isNetworkAvailable())
            throw UnknownHostException()
        val call = api.func(token).execute()
        if (call.code() == 200) {
            return call.body()
        } else {
            call.errorBody()?.string()?.let {
                if (it.contains("Session expired") || it.contains("Access is denied"))
                    throw ReAuthRequiredException()
            }
            throw InternalException(call.errorBody()?.string()
                    ?: "Ошибка получения данных от сервера")
        }
    }

}