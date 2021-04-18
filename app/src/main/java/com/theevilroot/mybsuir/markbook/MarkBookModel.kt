package com.theevilroot.mybsuir.markbook

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.common.data.MarkBook

class MarkBookModel (
        api: ApiService,
        store: CredentialsStore,
        applicationContext: Context,
        cacheManager: ICacheManager,
): ApiModel(api, store, applicationContext, cacheManager), IMarkBookModel {

    private var markBookCache: MarkBook? = null

    override fun getMarkBook(allowCache: Boolean): MarkBook? {
        return apiCall(ApiService::markBook, if (allowCache) markBookCache else null)
                ?.apply { markBookCache = this }
    }

}