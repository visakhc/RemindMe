package com.app.remindme.data.database

import androidx.lifecycle.LiveData
import com.app.remindme.data.model.EventsDayModel
import com.app.remindme.data.model.EventsModel

class Repository(private val userDao: Dao) {

    val readAllData = userDao.readAllData()

    suspend fun addUser(user: EventsModel) {
        userDao.addUser(user)
    }

    suspend fun deleteUser( ) {
        userDao.deleteAll( )
    }

     fun findEvent(date: Int, month: Int, year: Int): LiveData<List<EventsModel>> {
        return userDao.findEvent(date, month, year)
    }
     fun findEventDayInMonth( month: Int, year: Int): LiveData<List<EventsDayModel>> {
        return userDao.findEventDayInMonth( month, year)
    }
}