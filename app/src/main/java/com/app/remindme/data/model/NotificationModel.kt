package com.app.remindme.data.model

import java.io.Serializable

data class NotificationModel(
    val eventId: Long,
    val eventsModel: EventsModel,
    val hour: Int,
    val minute: Int,
    val deleteEvent: Boolean,
    val notificationAction: String? = "",
    val notificationExtraData: String? = "",
    val notificationActionMsg: String? = "",
) : Serializable
