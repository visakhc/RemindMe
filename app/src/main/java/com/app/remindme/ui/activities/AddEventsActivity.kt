package com.app.remindme.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.remindme.data.model.EventsModel
import com.app.remindme.databinding.ActivityAddEventsBinding
import com.app.remindme.services.NotifyEventService
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.app.remindme.utils.shortToast
import java.util.*

class AddEvents : AppCompatActivity() {
    private lateinit var viewModel: EventsViewModel

    private var binding: ActivityAddEventsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        initViews()
        handleEvents()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
    }

    private fun initViews() {
        binding?.inclLayout?.tvTitle?.text = "Add Events"

        val year = intent.getIntExtra("year", -1)
        val month = intent.getIntExtra("month", -1)
        val day = intent.getIntExtra("day", -1)

        if (year != -1 && month != -1 && day != -1) {
            binding?.datePicker?.updateDate(year, month, day)
        }

        val title = intent.getStringExtra("title") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val emoji = intent.getStringExtra("emoji") ?: ""

        binding?.etTitle?.setText(title)
        binding?.etDesc?.setText(description)
        binding?.etEmoji?.setText(emoji)
    }

    private fun handleEvents() {
        binding?.inclLayout?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.btSave?.setOnClickListener {
            val title = binding?.etTitle?.text.toString()
            val desc = binding?.etDesc?.text.toString()
            val emoji = binding?.etEmoji?.text.toString()

            if (title.isNotBlank() || desc.isNotBlank() || emoji.isNotBlank()) {
                onBackPressed()
            } else {
                shortToast("Please fill at least one field")
            }
        }
    }

    override fun onBackPressed() {
        val title = binding?.etTitle?.text.toString().trim()
        val desc = binding?.etDesc?.text.toString().trim()
        val emoji = binding?.etEmoji?.text.toString().trim()

        if (title.isNotBlank() || desc.isNotBlank() || emoji.isNotBlank()) {
            val day = binding?.datePicker?.dayOfMonth!!
            val month = binding?.datePicker?.month!!
            val year = binding?.datePicker?.year!!
            val hour =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) binding?.timePicker?.hour!! else binding?.timePicker?.currentHour!!
            val minute =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) binding?.timePicker?.minute!! else binding?.timePicker?.currentMinute!!


            val id = intent.getIntExtra("id", -11)

            if (intent.action == "edit" && id != -11) {
                if (title != intent.getStringExtra("title") ||
                    desc != intent.getStringExtra("description") ||
                    emoji != intent.getStringExtra("emoji")
                ) {
                    viewModel.updateEvent(id = id, title = title, description = desc, emoji = emoji)
                    shortToast("Event updated")
                }
            } else {
                viewModel.addEvent(EventsModel(day, month, year, title, desc, emoji))
                createEventRemainder(title, emoji, desc, day, month, year, hour, minute)
                //todo add option for user to pre notify 5 or 6 or 7 days before the event
                shortToast("Event added")
            }
        }
        super.onBackPressed()
    }

    private fun createEventRemainder(
        title: String,
        emoji: String,
        description: String,
        date: Int,
        month: Int,
        year: Int,
        hour: Int,
        minute: Int
    ) {
        val intent = Intent(this@AddEvents, NotifyEventService::class.java)
        intent.putExtra("title", title)
        intent.putExtra("emoji", emoji)
        intent.putExtra("description", description)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent =
            PendingIntent.getBroadcast(this@AddEvents, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val calendar = Calendar.getInstance()
        //   calendar.set(year, month, date, hour, minute, 0)
        /*val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val test: String = sdf.format(calendar.time)
        logThis(test)*/
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis + 5000, pendingIntent)
    }
}