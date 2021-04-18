package com.theevilroot.mybsuir.group

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.common.data.GroupInfo

class GroupModel (
        api: ApiService,
        store: CredentialsStore,
        applicationContext: Context,
        cacheManager: ICacheManager,
): ApiModel(api, store, applicationContext, cacheManager), IGroupModel {

    override fun getGroupInfo(allowCache: Boolean): GroupInfo? {
        return apiCall(ApiService::groupInfo, if (allowCache) cacheManager.groupInfo() else null)
                ?.apply { cacheManager.groupInfo(this) }
    }

}