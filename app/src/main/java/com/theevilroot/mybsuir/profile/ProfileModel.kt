package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.PersonalCV
import com.theevilroot.mybsuir.common.data.PersonalInformation

class ProfileModel (
    private val api: ApiService,
    private val store: CredentialsStore
) {

    private var personalCvCache: PersonalCV? = null
    private var personalInformationCache: PersonalInformation? = null

    fun getPersonalCV(): PersonalCV? {
        val token = store.getToken()
            ?: throw NoCredentialsException()
        val call = api.personalCV(token).execute()
        if (call.code() == 200) {
            return call.body()?.apply { personalCvCache = this }
        } else throw InternalException(call.errorBody()?.string()
            ?: "Ошибка получения данных от сервера")
    }

    fun getPersonalInformation(): PersonalInformation? {
        val token = store.getToken()
            ?: throw NoCredentialsException()
        val call = api.personalInformation(token).execute()
        if (call.code() == 200) {
            return call.body()?.apply { personalInformationCache = this }
        } else throw InternalException(call.errorBody()?.string()
            ?: "Ошибка получения данных от сервера")
    }
}