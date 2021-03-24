package com.theevilroot.mybsuir.common.data

import android.graphics.Color
import androidx.annotation.ColorInt

sealed class ScheduleType (val stringValue: String, @ColorInt val markColor: Int)

object Lecture: ScheduleType("ЛК", Color.GREEN)
object Practice: ScheduleType("ПЗ", Color.RED)
object Lab: ScheduleType("ЛР", Color.YELLOW)

class UnknownType(stringValue: String): ScheduleType(stringValue, Color.GRAY)