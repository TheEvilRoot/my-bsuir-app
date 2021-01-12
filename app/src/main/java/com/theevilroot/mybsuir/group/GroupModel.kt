package com.theevilroot.mybsuir.group

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.GroupInfo

class GroupModel (api: ApiService, store: CredentialsStore, applicationContext: Context): ApiModel(api, store, applicationContext), IGroupModel {

    private var groupInfoCache: GroupInfo? = null

    override fun getGroupInfo(allowCache: Boolean): GroupInfo? {
        return apiCall(ApiService::groupInfo, if (allowCache) groupInfoCache else null)
                ?.apply { groupInfoCache = this }
    }

}