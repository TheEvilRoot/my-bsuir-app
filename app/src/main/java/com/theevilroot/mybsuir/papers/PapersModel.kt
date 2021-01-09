package com.theevilroot.mybsuir.papers

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.Paper
import com.theevilroot.mybsuir.common.data.PaperPlaceCategory

class PapersModel (
    api: ApiService,
    store: CredentialsStore,
    applicationContext: Context
) : ApiModel(api, store, applicationContext) {

    private val papersCache: MutableList<Paper> = mutableListOf()
    private val placesCache: MutableList<PaperPlaceCategory> = mutableListOf()

    fun getPapers(allowCache: Boolean): List<Paper>? =
        apiCall(ApiService::certificate, if (allowCache && papersCache.isNotEmpty()) papersCache else null)
            ?.also { papersCache.clear(); papersCache.addAll(it) }

    fun getPlaces(allowCache: Boolean): List<PaperPlaceCategory>? =
            apiCall(ApiService::places, if (allowCache && placesCache.isNotEmpty()) placesCache else null)
                    ?.also { placesCache.clear(); placesCache.addAll(it) }

}