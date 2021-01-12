package com.theevilroot.mybsuir.markbook

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.MarkBook

class MarkBookModel (
        api: ApiService,
        store: CredentialsStore,
        applicationContext: Context
): ApiModel(api, store, applicationContext), IMarkBookModel {

    private var markBookCache: MarkBook? = null

    override fun getMarkBook(allowCache: Boolean): MarkBook? {
        return apiCall(ApiService::markBook, if (allowCache) markBookCache else null)
                ?.apply { markBookCache = this }
    }

}