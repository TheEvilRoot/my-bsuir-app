package com.theevilroot.mybsuir.schedule

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.DaySchedule
import java.util.*

class ScheduleModel (
    api: ApiService,
    store: CredentialsStore,
    context: Context
): ApiModel(api, store, context), IScheduleModel {

    override fun getDaySchedule(allowCache: Boolean, date: Date): DaySchedule? =
        apiCall({ api.daySchedule(it, "Fri Feb 12 2021") }, null)

}