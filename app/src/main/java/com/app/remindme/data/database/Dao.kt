package com.app.remindme.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.remindme.data.model.CalendarModel
import com.app.remindme.data.model.EventsDayModel
import com.app.remindme.data.model.EventsModel

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEvent(eventsModel: EventsModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCalendarData(eventsModel: MutableList<CalendarModel>)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<EventsModel>>

    @Query("DELETE FROM user_table")
    fun deleteAllEvents()

    @Query("UPDATE user_table SET title = :title, description = :description,emoji =:emoji WHERE id = :id")
    fun updateEvent(id: Int, title: String, description: String, emoji: String)

    @Query("SELECT * FROM user_table WHERE day = :date AND month = :month AND year = :year")
    fun findEvent(date: Int, month: Int, year: Int): LiveData<List<EventsModel>>

    @Query("SELECT day,emoji FROM user_table WHERE month = :month AND year = :year")
    fun findEventDayInMonth(month: Int, year: Int): LiveData<List<EventsDayModel>>

    @Query("SELECT * FROM calendar_table WHERE month = :month AND year = :year")
    suspend fun getCalendarData(month: Int, year: Int): List<CalendarModel>

    @Query("DELETE FROM calendar_table")
    fun deleteAllCalendarData()
}