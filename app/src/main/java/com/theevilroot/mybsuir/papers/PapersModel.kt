package com.theevilroot.mybsuir.papers

import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.Paper

class PapersModel (
    api: ApiService,
    store: CredentialsStore
) : ApiModel(api, store) {

    private val papersCache: MutableList<Paper> = mutableListOf()

    fun getPapers(allowCache: Boolean): List<Paper>? =
        apiCall(ApiService::certificate, if (allowCache && papersCache.isNotEmpty()) papersCache else null)
            ?.also { papersCache.clear(); papersCache.addAll(it) }

}