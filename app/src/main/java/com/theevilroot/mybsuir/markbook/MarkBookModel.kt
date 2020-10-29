package com.theevilroot.mybsuir.markbook

import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.MarkBook

class MarkBookModel (
        api: ApiService,
        store: CredentialsStore
): ApiModel(api, store) {

    private var markBookCache: MarkBook? = null

    fun getMarkBook(allowCache: Boolean): MarkBook? {
        return apiCall(ApiService::markBook, if (allowCache) markBookCache else null)
                ?.apply { markBookCache = this }
    }


}