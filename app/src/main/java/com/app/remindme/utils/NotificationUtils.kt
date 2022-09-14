package com.app.remindme.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.media.RingtoneManager
import android.os.Build
import java.io.File


const val DISMISS_ACTION = "DISMISS"
const val PRIMARY_ACTION = "PRIMARY"
const val SECONDARY_ACTION = "SECONDARY"

fun Context.getNotificationSound(): String? {
    val nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = nManager.getNotificationChannel(USERDATA.NOTIFICATION_CHANNEL_ID)
        if (notificationChannel != null) {
            RingtoneManager.getRingtone(this, notificationChannel.sound).getTitle(this)
                ?.let { title ->
                    if (title.contains("default", true)) {
                        title
                    } else {
                        File(title).nameWithoutExtension
                    }
                }
        } else {
            val newChannel = NotificationChannel(
                USERDATA.NOTIFICATION_CHANNEL_ID,
                "RemindMe Events",
                NotificationManager.IMPORTANCE_HIGH
            )
            nManager.createNotificationChannel(newChannel)
            getNotificationSound()
        }
    } else null
}