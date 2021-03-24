package com.theevilroot.mybsuir.schedule

import com.theevilroot.mybsuir.common.data.DaySchedule

interface IScheduleModel {

    fun getDaySchedule(allowCache: Boolean): DaySchedule?

}