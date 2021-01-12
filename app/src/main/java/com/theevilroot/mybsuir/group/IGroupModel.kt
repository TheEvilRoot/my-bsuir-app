package com.theevilroot.mybsuir.group

import com.theevilroot.mybsuir.common.data.GroupInfo

interface IGroupModel {
    fun getGroupInfo(allowCache: Boolean): GroupInfo?
}