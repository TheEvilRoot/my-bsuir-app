package com.theevilroot.mybsuir.common

import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.ReAuthRequiredException
import retrofit2.Call

open class  ApiModel (
        val api: ApiService,
        val store: CredentialsStore
) {

    fun <T> apiCall(func: ApiService.(String) -> Call<T>, cachedData: T? = null): T? {
        if (cachedData != null)
            return cachedData
        val token = store.getToken()
                ?: throw NoCredentialsException()
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