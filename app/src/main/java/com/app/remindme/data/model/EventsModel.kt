package com.app.remindme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class EventsModel(
    val title: String,
    val description: String,
    @PrimaryKey(autoGenerate = true) var id: Int? = null

)