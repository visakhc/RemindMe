package com.app.remindme.utils

import java.text.SimpleDateFormat
import java.util.*


fun getDayFormatted(pattern: String, instance: Calendar = Calendar.getInstance()): String {
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(instance.time).toString()
}