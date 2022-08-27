package com.app.remindme.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.app.remindme.R
import com.app.remindme.data.model.NotificationModel
import com.app.remindme.ui.activities.MainActivity
import com.app.remindme.utils.USERDATA.NOTIFICATION_CHANNEL_ID
import com.app.remindme.utils.logThis


class NotifyEventService : BroadcastReceiver() {
    private val default_notification_channel_id = "default"


    override fun onReceive(context: Context?, intent: Intent?) {
        with(context!!) {
            val data = intent?.getBundleExtra("data")
                ?.getSerializable("notificationModel") as NotificationModel
            var title = data.eventsModel.title
            var emoji = data.eventsModel.emoji
            var description = data.eventsModel.description
            title =
                if (title == "" || title == " ") "An Event is coming " else title
            emoji = if (emoji == "" || emoji == " ") "" else emoji
            description =
                if (description == "" || description == " ") "tap to know more" else description

            //logThis("[notification] title: $title emoji: $emoji description: $description ")
            logThis("[notification] $data ")

            val notificationIntent = when (data.notificationAction) {
                "Default" -> Intent(context, MainActivity::class.java)
                "Whatsapp" -> {
                    val url =
                        "https://api.whatsapp.com/send?phone=+91${data.notificationExtraData}&text=${data.notificationActionMsg}".toUri()
                    logThis("Trying to open Whatsapp $url")
                    Intent(Intent.ACTION_VIEW).also { it.data = url }
                }
                "Instagram" -> Intent(context, MainActivity::class.java) //todo
                else -> {
                    Intent(context, MainActivity::class.java)
                }
            }
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val mBuilder = NotificationCompat.Builder(context, default_notification_channel_id)
            mBuilder.setContentTitle(title + emoji)
            mBuilder.setContentIntent(pendingIntent)
            mBuilder.setContentText(description)
            mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
            mBuilder.setAutoCancel(true)
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
            /*   if (getSessionData("openedEvent${data.id}",false)){
                   if (data?.deleteEvent==true){
                       deleteEvent(data.id)
                   }todo do this in a separate service which handles events that are opened and unopened
               }else{
                   sendNotification(" ")
               }*/
        }
    }
}