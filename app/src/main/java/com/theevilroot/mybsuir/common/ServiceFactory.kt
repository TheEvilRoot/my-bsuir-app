package com.theevilroot.mybsuir.common

import com.google.gson.GsonBuilder
import com.theevilroot.mybsuir.common.data.ScheduleType
import com.theevilroot.mybsuir.common.typeadapters.GsonScheduleTypeAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceFactory {

    inline fun <reified T> createService(baseUrl: String, client: OkHttpClient): T =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(ScheduleType::class.java, GsonScheduleTypeAdapter)
                        .create()
                )).client(client)
                .build()
                .create(T::class.java)


}