package com.app.remindme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class EventsModel(
    val day: Int,
    val month: Int,
    val year: Int,
    val title: String,
    val description: String,
    val emoji: String = "",
    @PrimaryKey(autoGenerate = true) var id: Int? = null
):Serializable

data class EventsDayModel(
    val day: Int,
    val emoji: String = "",
)


@Entity(tableName = "calendar_table")
data class CalendarModel(
    val date: Int,
    val day: String,
    val month: Int,
    val year: Int,
    var emoji: String = "",
    @PrimaryKey(autoGenerate = true) var id: Int? = null
)

data class ContactModel(
    val name: String,
    val number: String,
)