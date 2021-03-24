package com.theevilroot.mybsuir.common.data

data class DaySchedule (
    val studentGroup: Group,
    val todayDate: String,
    val todaySchedules: List<ScheduleEntry>,
    val currentWeek: Int,
    val dateStart: String,
    val dateEnd: String
)