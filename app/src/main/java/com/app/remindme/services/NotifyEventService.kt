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
import com.app.remindme.R
import com.app.remindme.ui.activities.MainActivity
import com.app.remindme.utils.logThis


class NotifyEventService : BroadcastReceiver() {
    val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"


    override fun onReceive(context: Context?, intent: Intent?) {
        with(context!!) {
            var title = intent?.getStringExtra("title")
            title =
                if (title == null || title == "" || title == " ") "An Event is coming " else title

            var emoji = intent?.getStringExtra("emoji")
            emoji = if (emoji == null || emoji == "" || emoji == " ") "‼" else emoji
//todo fix notification title and emoji not showing correctly
            var description = intent?.getStringExtra("description")
            description =
                if (description == null || description == "" || description == " ") "tap to know more" else description

            logThis(
                "WE REACH ON BIND  $title $emoji $description ${title.length} ${emoji.length} ${description.length} "
            )

            val notificationIntent = Intent(context, MainActivity::class.java)
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "RemindMe Events",
                    importance
                )
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                mNotificationManager.createNotificationChannel(notificationChannel)
            }
            mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
            //  throw UnsupportedOperationException("Not yet implemented")
        }
    }
}
