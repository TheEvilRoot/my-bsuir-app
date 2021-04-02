package com.theevilroot.mybsuir.common.utils

import java.text.SimpleDateFormat
import java.util.*

//Fri%20Feb%2012%202021
private val scheduleDateFormat = SimpleDateFormat("EEE MMM dd yyyy", Locale.US)

fun Date.asScheduleDate(): String {
    return scheduleDateFormat.format(this)
}