package com.theevilroot.mybsuir.announcements

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.common.data.Announcement

class AnnouncementsModel(
        api: ApiService,
        store: CredentialsStore,
        context: Context,
        cacheManager: ICacheManager
) : IAnnouncementsModel, ApiModel(api, store, context, cacheManager) {

    private var announcementsCache: List<Announcement>? = null

    override fun getAnnouncements(allowCache: Boolean): List<Announcement>? =
            apiCall(ApiService::announcements, if (allowCache) announcementsCache else null)
                    .also { announcementsCache = it }

}