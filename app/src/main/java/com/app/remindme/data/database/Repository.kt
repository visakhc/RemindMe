package com.app.remindme.data.database

import com.app.remindme.data.model.EventsModel

class Repository(private val userDao: Dao) {

    val readAllData = userDao.readAllData()

    suspend fun addUser(user: EventsModel) {
        userDao.addUser(user)
    }

    suspend fun deleteUser(id: Int) {
        userDao.deleteUser(id)
    }

/*    @WorkerThread
    suspend fun searchUser(searchText: String): LiveData<List<EventsModel>> {
        return userDao.searchUser(searchText)
    }*/
}