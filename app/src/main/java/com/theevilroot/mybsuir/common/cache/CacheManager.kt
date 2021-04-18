package com.theevilroot.mybsuir.common.cache

import com.theevilroot.mybsuir.common.data.DaySchedule
import com.theevilroot.mybsuir.common.data.GroupInfo
import com.theevilroot.mybsuir.common.data.PersonalCV
import com.theevilroot.mybsuir.common.data.PersonalInformation

class CacheManager : ICacheManager {

    private var dayScheduleMap = mutableMapOf<String, DaySchedule>()
    private var personalCvCache: PersonalCV? = null
    private var personalInformationCache: PersonalInformation? = null
    private var groupInfoCache: GroupInfo? = null

    override fun dayScheduleCache(key: String, daySchedule: DaySchedule) {
        dayScheduleMap[key] = daySchedule
    }

    override fun dayScheduleCache(key: String): DaySchedule? {
        return dayScheduleMap[key]
    }

    override fun personalCv(): PersonalCV? {
        return personalCvCache
    }

    override fun personalCv(personalCV: PersonalCV) {
        personalCvCache = personalCV
    }

    override fun personalInformation(): PersonalInformation? {
        return personalInformationCache
    }

    override fun personalInformation(personalInformation: PersonalInformation) {
        personalInformationCache = personalInformation
    }

    override fun groupInfo(): GroupInfo? {
        return groupInfoCache
    }

    override fun groupInfo(groupInfo: GroupInfo) {
        groupInfoCache = groupInfo
    }

    override fun markDirty() {
        dayScheduleMap.clear()
        personalCvCache = null
        personalInformationCache = null
        groupInfoCache = null
    }

}