package com.theevilroot.mybsuir.schedule

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.DaySchedule
import com.theevilroot.mybsuir.common.utils.asScheduleDate
import java.util.*

class ScheduleModel (
    api: ApiService,
    store: CredentialsStore,
    context: Context
): ApiModel(api, store, context), IScheduleModel {

    private val dayScheduleCache = mutableMapOf<String, DaySchedule>()

    override fun getDaySchedule(allowCache: Boolean, date: Date): DaySchedule? = date.asScheduleDate().let { dateStr ->
        apiCall({ api.daySchedule(it, dateStr) }, if (allowCache) dayScheduleCache[dateStr] else null)
            ?.also { dayScheduleCache[dateStr] = it }
    }

}