package com.app.remindme.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.app.remindme.data.database.DatabaseBuilder
import com.app.remindme.data.database.Repository
import com.app.remindme.data.model.CalendarModel
import com.app.remindme.data.model.EventsDayModel
import com.app.remindme.data.model.EventsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventsViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<EventsModel>>

    private val repository: Repository

    init {
        val userDao = DatabaseBuilder.getDatabase(application).userDao()
        repository = Repository(userDao)
        readAllData = repository.readAllData
    }

    fun addEvent(event: EventsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEvent(event)
        }
    }


    fun addCalendarData(calendar: MutableList<CalendarModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCalendarData(calendar)
        }
    }

    fun deleteEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEvent()
        }
    }

    fun deleteAllCalendarData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllCalendarData()
        }
    }

    fun updateEvent(id: Int, title: String, description: String, emoji: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEvent(id, title, description, emoji)
        }
    }

    fun findEvent(date: Int, month: Int, year: Int): LiveData<List<EventsModel>> {
        return repository.findEvent(date, month, year)
    }

    fun findEventDayInMonth(month: Int, year: Int): LiveData<List<EventsDayModel>> {
        return repository.findEventDayInMonth(month, year)
    }

    /*  fun getCalendarData(month: Int, year: Int): LiveData<List<CalendarModel>> {
          return repository.getCalendarData(month, year)
      }*/
    suspend fun getCalendarData(month: Int, year: Int): List<CalendarModel> {
        return repository.getCalendarData(month, year)
    }
}