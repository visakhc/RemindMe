package com.app.remindme.services

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.remindme.data.model.NotificationModel
import com.app.remindme.utils.PRIMARY_ACTION
import com.app.remindme.utils.SECONDARY_ACTION
import com.app.remindme.utils.logThis
import java.util.*


class NotificationClickListener : BroadcastReceiver() {
    private val default_notification_channel_id = "default"


    override fun onReceive(context: Context?, intent: Intent?) {

        logThis("Notification Clicked:::::::  ${intent?.action}    ")


        context?.apply {
            val notificationId = intent?.getIntExtra("notificationId", -1) ?: -1
            val notificationModel = intent?.getSerializableExtra("data") as NotificationModel
            if (notificationId != -1) {
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationId)
            }
            when (intent.action) {
                PRIMARY_ACTION -> {
                    createEventReminder(notificationModel, 1800000) // 30 min snooze
                }
                SECONDARY_ACTION -> {
                    createEventReminder(notificationModel, 3600000) // 1 hour snooze
                }
            }
        }
        /*     with(context!!) { //todo working on here
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

                 val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                 val mBuilder = NotificationCompat.Builder(context, default_notification_channel_id)
                 mBuilder.setContentTitle(title + emoji)
                 mBuilder.setContentIntent(pendingIntent)
                 mBuilder.setContentText(description)
                 mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
                 mBuilder.setAutoCancel(true)
                 mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)


                 val primaryAction = Intent(this@with, NotifyEventService::class.java)
                 primaryAction.action = PRIMARY_ACTION
                 val pendingIntentYes = PendingIntent.getBroadcast(
                     this,
                     12345,
                     primaryAction,
                     pendingIntentFlags
                 )
                 mBuilder.addAction(R.drawable.ic_add, "snooze after 30 minutes", pendingIntentYes)

                 val secondaryAction = Intent()
                 secondaryAction.action = SECONDARY_ACTION
                 val pendingIntentMaybe = PendingIntent.getBroadcast(
                     this,
                     12345,
                     secondaryAction,
                     pendingIntentFlags
                 )
                 mBuilder.addAction(R.drawable.ic_arrow_forward, "after 1 hour", pendingIntentMaybe)

                 val dismissAction = Intent()
                 dismissAction.action = DISMISS_ACTION
                 val pendingIntentNo = PendingIntent.getBroadcast(
                     this,
                     12345,
                     dismissAction,
                     pendingIntentFlags
                 )
                 mBuilder.addAction(
                     androidx.recyclerview.R.drawable.notification_bg_low,
                     "Dismiss",
                     pendingIntentNo
                 )

             //    mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())

                 if (data.deleteEvent) {
                     CoroutineScope(Dispatchers.IO).launch {
                         val userDao = DatabaseBuilder.getDatabase(context).userDao()
                         val repository = Repository(userDao)
                         repository.deleteEvents(data.eventId)
                     }
                 }
             }*/
    }

    private fun Context.createEventReminder(
        notificationModel: NotificationModel,
        snoozeTime: Long
    ) {
        val args = Bundle()
        args.putSerializable("notificationModel", notificationModel)
        val intent = Intent(this, NotifyEventService::class.java)
        intent.putExtra("data", args)
        val alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            pendingIntentFlags
        )
        val calendar = Calendar.getInstance()
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis + snoozeTime,
            pendingIntent
        )
    }
}