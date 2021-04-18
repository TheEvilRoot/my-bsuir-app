package com.theevilroot.mybsuir.schedule

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.common.data.DaySchedule
import com.theevilroot.mybsuir.common.utils.asScheduleDate
import java.util.*

class ScheduleModel (
    api: ApiService,
    store: CredentialsStore,
    context: Context,
    cacheManager: ICacheManager
): ApiModel(api, store, context, cacheManager), IScheduleModel {

    override fun getDaySchedule(allowCache: Boolean, date: Date): DaySchedule? = date.asScheduleDate().let { dateStr ->
        apiCall({ api.daySchedule(it, dateStr) }, if (allowCache) cacheManager.dayScheduleCache(dateStr) else null)
            ?.also { cacheManager.dayScheduleCache(dateStr, it) }
    }

}