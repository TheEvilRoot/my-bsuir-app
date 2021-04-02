package com.theevilroot.mybsuir.schedule

import com.theevilroot.mybsuir.common.data.DaySchedule
import java.util.*

interface IScheduleModel {

    fun getDaySchedule(allowCache: Boolean, date: Date): DaySchedule?

}