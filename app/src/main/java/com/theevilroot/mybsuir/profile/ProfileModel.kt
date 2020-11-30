package com.theevilroot.mybsuir.profile

import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.*

class ProfileModel(api: ApiService, store: CredentialsStore): ApiModel(api, store) {

    private var personalCvCache: PersonalCV? = null
    private var personalInformationCache: PersonalInformation? = null

    fun getPersonalCV(allowCache: Boolean): PersonalCV? {
        return apiCall(ApiService::personalCV, if (allowCache) personalCvCache else null)
                ?.apply { personalCvCache = this }
    }

    fun getPersonalInformation(allowCache: Boolean): PersonalInformation? {
        return apiCall(ApiService::personalInformation, if (allowCache) personalInformationCache else null)
                ?.apply { personalInformationCache = this }
    }
}