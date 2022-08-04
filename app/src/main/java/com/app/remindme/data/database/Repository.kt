package com.app.remindme.data.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.app.remindme.data.model.EventsModel

class Repository(private val userDao: Dao) {

    val readAllData = userDao.readAllData()

    suspend fun addUser(user: EventsModel) {
        userDao.addUser(user)
    }

    suspend fun deleteUser(id: Int) {
        userDao.deleteUser(id)
    }

    @WorkerThread
    suspend fun findEvent(date: Int, month: Int, year: Int): EventsModel{
        try {
            userDao.findEvent(/*date*//*, month, year*/)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return userDao.findEvent(/*date*//*, month, year*/)
    }
}