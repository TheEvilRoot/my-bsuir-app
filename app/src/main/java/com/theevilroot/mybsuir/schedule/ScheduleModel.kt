package com.theevilroot.mybsuir.schedule

import android.content.Context
import com.theevilroot.mybsuir.common.ApiModel
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.CredentialsStore
import com.theevilroot.mybsuir.common.data.DaySchedule

class ScheduleModel (
    api: ApiService,
    store: CredentialsStore,
    context: Context
): ApiModel(api, store, context), IScheduleModel {

    override fun getDaySchedule(allowCache: Boolean): DaySchedule? =
        apiCall(ApiService::daySchedule, null)

}