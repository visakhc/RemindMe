package com.app.remindme.data.database

import androidx.lifecycle.LiveData
import com.app.remindme.data.model.CalendarModel
import com.app.remindme.data.model.EventsDayModel
import com.app.remindme.data.model.EventsModel

class Repository(private val userDao: Dao) {

    val readAllData = userDao.readAllData()

    suspend fun addEvent(user: EventsModel) {
        userDao.addEvent(user)
    }

    suspend fun addCalendarData(calendar: CalendarModel) {
        userDao.addCalendarData(calendar)
    }

    suspend fun deleteEvent() {
        userDao.deleteAll()
    }

    suspend fun updateEvent(id: Int, title: String, description: String, emoji: String) {
        userDao.updateEvent(id, title, description, emoji)
    }

    fun findEvent(date: Int, month: Int, year: Int): LiveData<List<EventsModel>> {
        return userDao.findEvent(date, month, year)
    }

    fun findEventDayInMonth(month: Int, year: Int): LiveData<List<EventsDayModel>> {
        return userDao.findEventDayInMonth(month, year)
    }

    fun getCalendarData(month: Int, year: Int): LiveData<List<CalendarModel>> {
        return userDao.getCalendarData(month, year)
    }
}