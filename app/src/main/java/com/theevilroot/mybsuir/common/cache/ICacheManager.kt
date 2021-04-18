package com.theevilroot.mybsuir.common.cache

import com.theevilroot.mybsuir.common.data.DaySchedule
import com.theevilroot.mybsuir.common.data.GroupInfo
import com.theevilroot.mybsuir.common.data.PersonalCV
import com.theevilroot.mybsuir.common.data.PersonalInformation

interface ICacheManager {

    fun dayScheduleCache(key: String, daySchedule: DaySchedule)

    fun dayScheduleCache(key: String): DaySchedule?

    fun personalCv(): PersonalCV?

    fun personalCv(personalCV: PersonalCV)

    fun personalInformation(): PersonalInformation?

    fun personalInformation(personalInformation: PersonalInformation)

    fun groupInfo(): GroupInfo?

    fun groupInfo(groupInfo: GroupInfo)

    fun markDirty()

}