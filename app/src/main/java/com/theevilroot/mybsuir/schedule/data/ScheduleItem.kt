package com.theevilroot.mybsuir.schedule.data

import com.theevilroot.mybsuir.common.data.ScheduleEntry

sealed class ScheduleItem {
    class RestInterval(val start: String?, val title: String, val end: String?): ScheduleItem()
    class LessonInterval(val entry: ScheduleEntry): ScheduleItem()
}