package com.app.remindme.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.remindme.data.model.EventsModel

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: EventsModel)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<EventsModel>>

    @Query("DELETE FROM user_table WHERE id = :userId")
    fun deleteUser(userId: Int)

    @Query("SELECT * FROM user_table WHERE day = :date AND month = :month AND year = :year")
      fun findEvent(date: Int, month: Int, year: Int):LiveData<List<EventsModel>>

}