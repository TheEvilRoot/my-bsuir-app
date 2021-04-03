package com.theevilroot.mybsuir.schedule.data

import com.theevilroot.mybsuir.common.data.ScheduleEntry
import com.theevilroot.mybsuir.common.data.SimpleTime

sealed class ScheduleItem {
    class RestInterval(val start: SimpleTime?, val title: String, val end: SimpleTime?): ScheduleItem()
    class LessonInterval(val entry: ScheduleEntry, val isTop: Boolean? = false): ScheduleItem()
}