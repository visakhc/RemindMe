package com.app.remindme.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.remindme.data.database.DatabaseBuilder
import com.app.remindme.data.database.Repository
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

    fun addEvent(user: EventsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(id)
        }
    }

    fun findEvent(date: Int, month: Int, year: Int): EventsModel?{
         var ls:  EventsModel? = null
        viewModelScope.launch(Dispatchers.IO) {
            ls = repository.findEvent(date, month, year)
        }
        return ls
    }
}