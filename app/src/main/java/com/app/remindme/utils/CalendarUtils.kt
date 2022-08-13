package com.app.remindme.utils

import java.text.SimpleDateFormat
import java.util.*


fun getDayFormatted(pattern: String, instance: Calendar = Calendar.getInstance()): String {
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(instance.time).toString()
}


fun monthInWords(
    month: Int,
    monthStyle: MonthStyle = MonthStyle.FULL,
    textStyle: TextStyle = TextStyle.DEFAULT
): String {

    var text = ""
    text = when (month) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> "Invalid month"
    }
    text = if (monthStyle == MonthStyle.SHORT) text.substring(0, 3) else text
    text = if (textStyle == TextStyle.ALL_CAPS) text.uppercase() else text
    text = if (textStyle == TextStyle.ALL_SMALL) text.lowercase() else text
    return text
}

enum class MonthStyle {
    SHORT,
    FULL
}

enum class TextStyle {
    ALL_CAPS,
    ALL_SMALL,
    DEFAULT
}


