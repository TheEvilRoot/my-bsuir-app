package com.theevilroot.mybsuir.marksheets

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.common.data.MarkSheet

class MarkSheetsModel(
        api: ApiService,
        store: CredentialsStore,
        context: Context,
        cacheManager: ICacheManager,
        ) : ApiModel(api, store, context, cacheManager), IMarkSheetsModel {

    private val markSheetsCache: MutableList<MarkSheet> = mutableListOf()

    override fun getMarkSheets(allowCache: Boolean): List<MarkSheet>? =
        apiCall(ApiService::markSheet, if (allowCache && markSheetsCache.isNotEmpty()) markSheetsCache else null)
                ?.also {
                    if (!allowCache || markSheetsCache.isEmpty()) {
                        markSheetsCache.clear()
                        markSheetsCache.addAll(it)
                    }
                }


}