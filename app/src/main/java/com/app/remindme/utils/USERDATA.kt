package com.app.remindme.utils

import java.util.*

object USERDATA {
    private val c: Calendar = Calendar.getInstance()
    val thisYear: Int = c.get(Calendar.YEAR)
    val thisMonth: Int = c.get(Calendar.MONTH)
    val thisDay = c.get(Calendar.DATE)
}