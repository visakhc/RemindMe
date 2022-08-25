package com.app.remindme.data.model

import java.io.Serializable

data class NotificationModel(
    val title: String? = "",
    val emoji: String? = "",
    val description: String? = "",
    val date: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int,
    val deleteEvent: Boolean,
    val notificationAction: String? = "",
    val notificationExtraData: String? = "",
    val notificationActionMsg: String? = ""
) : Serializable
