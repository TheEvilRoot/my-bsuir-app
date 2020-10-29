package com.theevilroot.mybsuir.group

import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.GroupInfo

class GroupModel (api: ApiService, store: CredentialsStore): ApiModel(api, store) {

    private var groupInfoCache: GroupInfo? = null

    fun getGroupInfo(allowCache: Boolean): GroupInfo? {
        return apiCall(ApiService::groupInfo, if (allowCache) groupInfoCache else null)
                ?.apply { groupInfoCache = this }
    }

}