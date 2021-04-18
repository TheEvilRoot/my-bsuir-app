package com.theevilroot.mybsuir.preferences

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.cache.ICacheManager

class PreferencesModel (
        api: ApiService,
        store: CredentialsStore,
        appContext: Context,
        cacheManager: ICacheManager,
) : ApiModel(api, store, appContext, cacheManager), IPreferencesModel {

    override fun savePreference(field: String, key: String, value: Boolean): Boolean? {
        return apiCall({ api.savePreference(it, field, mapOf(key to value)) }, null)
            ?.let { value }
    }

}