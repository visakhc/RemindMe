package com.app.remindme.services

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
import com.app.remindme.data.database.DatabaseBuilder
import com.app.remindme.data.database.Repository
import com.app.remindme.data.model.NotificationModel
import com.app.remindme.ui.activities.MainActivity
import com.app.remindme.utils.DISMISS_ACTION
import com.app.remindme.utils.PRIMARY_ACTION
import com.app.remindme.utils.SECONDARY_ACTION
import com.app.remindme.utils.USERDATA.NOTIFICATION_CHANNEL_ID
import com.app.remindme.utils.logThis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotifyEventService : BroadcastReceiver() {
    private val default_notification_channel_id = "default"


    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            with(ctx) {
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

                logThis("[notification] $data ")
                // todo do this in a separate service which handles events that are opened and unopened

                val notificationIntent = when (data.notificationAction) {
                    "Default" -> Intent(context, MainActivity::class.java)
                    "Whatsapp" -> {
                        val url =
                            "https://api.whatsapp.com/send?phone=${data.notificationExtraData}&text=${data.notificationActionMsg}".toUri()
                        logThis("Trying to open Whatsapp $url")
                        Intent(Intent.ACTION_VIEW).also { it.data = url }
                    }
                    "Instagram" -> {
                        val url = "https://instagram.com/${data.notificationExtraData}".toUri()
                        logThis("Trying to open Instagram $url")
                        Intent(Intent.ACTION_VIEW).also { it.data = url }
                    }
                    else -> {
                        Intent(context, MainActivity::class.java)
                    }
                }
                notificationIntent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                val pendingIntent =
                    PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        pendingIntentFlags
                    )

                val mNotificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val mBuilder = NotificationCompat.Builder(context, default_notification_channel_id)
                mBuilder.setContentTitle(title + emoji)
                mBuilder.setContentIntent(pendingIntent)
                mBuilder.setContentText(description)
                mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
                mBuilder.setAutoCancel(true)
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)

                val notifId = System.currentTimeMillis().toInt()

                val intentAction = Intent(this@with, NotificationClickListener::class.java)
                intentAction.putExtra("notificationId", notifId)
                /* val args = Bundle()
                 args.putSerializable("notificationModel", data)*/
                intentAction.putExtra("data", data)

                val pendingIntentDismiss = PendingIntent.getBroadcast(
                    this,
                    12345,
                    intentAction.also { it.action = DISMISS_ACTION },
                    pendingIntentFlags
                )
                mBuilder.addAction(R.drawable.ic_add_outline, "dismiss", pendingIntentDismiss)

                val pendingIntentPri = PendingIntent.getBroadcast(
                    this,
                    12345,
                    intentAction.also { it.action = PRIMARY_ACTION },
                    pendingIntentFlags
                )
                mBuilder.addAction(R.drawable.ic_add, "snooze for 30m", pendingIntentPri)

                val pendingIntentSec = PendingIntent.getBroadcast(
                    this,
                    12345,
                    intentAction.also { it.action = SECONDARY_ACTION },
                    pendingIntentFlags
                )
                mBuilder.addAction(R.drawable.ic_arrow_forward, "1 hour", pendingIntentSec)


                mNotificationManager.notify(notifId, mBuilder.build())

                if (data.deleteEvent) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val userDao = DatabaseBuilder.getDatabase(context).userDao()
                        val repository = Repository(userDao)
                        repository.deleteEvents(data.eventId)
                    }
                }
            }
        }
    }
}

val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
} else {
    PendingIntent.FLAG_UPDATE_CURRENT
}
