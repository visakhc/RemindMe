package com.app.remindme.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.remindme.R
import com.app.remindme.adapter.ContactsAdapter
import com.app.remindme.data.model.ContactModel
import com.app.remindme.data.model.EventsModel
import com.app.remindme.data.model.NotificationModel
import com.app.remindme.databinding.ActivityAddEventsBinding
import com.app.remindme.services.NotifyEventService
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.app.remindme.utils.*
import com.app.remindme.utils.USERDATA.NOTIFICATION_CHANNEL_ID
import contacts.async.findAsync
import contacts.core.Contacts
import contacts.core.util.phoneList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class AddEventsActivity : AppCompatActivity(), ContactsAdapter.ContactItemClickListener {
    private lateinit var viewModel: EventsViewModel
    private var whatsappNum: String? = null
    private val contactsAdapter by lazy { ContactsAdapter(this@AddEventsActivity) }

    private lateinit var binding: ActivityAddEventsBinding
    override fun onResume() {
        super.onResume()
        binding.tvNotificationSound.text = getNotificationSound() ?: "Default"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initViews()
        handleEvents()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        binding.rvContacts.apply {
            layoutManager = LinearLayoutManager(this@AddEventsActivity)
            adapter = contactsAdapter
        }
    }

    private fun initViews() {
        binding.inclLayout.tvTitle.text = "Add Events"

        val year = intent.getIntExtra("year", -1)
        val month = intent.getIntExtra("month", -1)
        val day = intent.getIntExtra("day", -1)

        if (year != -1 && month != -1 && day != -1) {
            binding.datePicker.updateDate(year, month, day)
        }

        val title = intent.getStringExtra("title") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val emoji = intent.getStringExtra("emoji") ?: ""

        binding.etTitle.setText(title)
        binding.etDesc.setText(description)
        binding.etEmoji.setText(emoji)
        binding.tvNotificationSound.isSelected = true
        //todo add option to sync contacts
    }

    private fun handleEvents() {
        binding.inclLayout.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val desc = binding.etDesc.text.toString().trim()
            val emoji = binding.etEmoji.text.toString().trim()

            if (title.isNotBlank()) {
                onBackPressed()
            } else {
                binding.etTitle.error = "Title is required"
                // shortToast("Please fill at least one field")
            }
        }

        binding.layoutNotificationSound.setOnClickListener {
            /*         val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                     intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                     intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
                     intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
                     startActivityForResult(intent, 5)*/
            /*   val intent: Intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                   .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                   .putExtra(Settings.EXTRA_CHANNEL_ID, NOTIFICATION_CHANNEL_ID)
               startActivity(intent)*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, NOTIFICATION_CHANNEL_ID)
                startActivity(intent)
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }

        binding.swSendNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutNotificationSettings.show()
            } else {
                binding.layoutNotificationSettings.hide()
            }
        }

        binding.tvNotificationAction.setOnClickListener {
            showPopupMenu(it, R.menu.notication_action_menu)
        }

        binding.etNotificationActionExtra.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.isNotBlank() && text.length > 2) {
                lifecycleScope.launch {
                    val nameList = findContactsName(text)
                    nameList.isNotEmpty()? binding.rvContacts.show():      binding.rvContacts.hide()

                    contactsAdapter.updateList(nameList)
                }
            } else {
                whatsappNum = null
            }
        }
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                val uri =
                    data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) ?: Uri.EMPTY
                binding.tvNotificationSound.text =
                    RingtoneManager.getRingtone(this, uri).getTitle(this)
                RingtoneManager.getRingtone(this, uri).getTitle(this)
                saveSessionData("notification_uri", uri)
                uri.toLog()
            }
        }
    }*/

    override fun onBackPressed() {
        val title = binding.etTitle.text.toString().trim()
        val desc = binding.etDesc.text.toString().trim()
        val emoji = binding.etEmoji.text.toString().trim()

        if (desc.isNotBlank() || emoji.isNotBlank()) {
            if (title.isNotBlank()) {
                val day = binding.datePicker.dayOfMonth
                val month = binding.datePicker.month
                val year = binding.datePicker.year
                val hour =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) binding.timePicker.hour else binding.timePicker.currentHour
                val minute =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) binding.timePicker.minute else binding.timePicker.currentMinute

                val id = intent.getIntExtra("id", -11)
                val deleteEvent = binding.swDeleteEvent.isChecked
                val notificationAction = binding.tvNotificationAction.text.toString()
                val notificationExtraData =
                    whatsappNum ?: binding.etNotificationActionExtra.text.toString()
                val notificationActionMsg = binding.etMessage.text.toString()

                if (intent.action == "edit" && id != -11) {
                    if (title != intent.getStringExtra("title") ||
                        desc != intent.getStringExtra("description") ||
                        emoji != intent.getStringExtra("emoji")
                    ) {
                        viewModel.updateEvent(
                            id = id,
                            title = title,
                            description = desc,
                            emoji = emoji
                        )
                        //create or edit todo event remainder for this with update an already existing
                        shortToast("Event updated")
                    }
                } else {
                    val eventData = EventsModel(day, month, year, title, desc, emoji)
                    lifecycle.coroutineScope.launch {
                        val eventId = viewModel.addEvent(eventData)
                        if (binding.swSendNotification.isChecked)
                            createEventReminder(
                                NotificationModel(
                                    eventId,
                                    eventData,
                                    hour, minute, deleteEvent, notificationAction,
                                    notificationExtraData, notificationActionMsg
                                )
                            )
                    }
                    //todo add option for user to pre notify 5 or 6 or 7 days before the event
                    shortToast("Event added")
                }
            } else binding.etTitle.error = "Title is required"
        }
        super.onBackPressed()
    }

    private fun createEventReminder(notificationModel: NotificationModel) {
        val args = Bundle()
        args.putSerializable("notificationModel", notificationModel)
        val intent = Intent(this@AddEventsActivity, NotifyEventService::class.java)
        intent.putExtra("data", args)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent =
            PendingIntent.getBroadcast(
                this@AddEventsActivity,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val calendar = Calendar.getInstance()
        //   calendar.set(year, month, date, hour, minute, 0)
        /*val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val test: String = sdf.format(calendar.time)
        logThis(test)*/
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis + 200,
            pendingIntent
        )
    }


    private fun showPopupMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this@AddEventsActivity, view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {
            saveSessionData("notification_action", it.title.toString()) //todo remove
            binding.tvNotificationAction.text = it.title
            when (it.title) {
                "Default" -> {
                    binding.layoutNotificationActionExtra.hide()
                }
                "Whatsapp" -> {
                    binding.etMessage.show()
                    binding.layoutNotificationActionExtra.show()
                    binding.tvNotificationActionExtra.text = "Whatsapp Number"
                }
                "Instagram" -> {
                    binding.etMessage.hide()
                    binding.layoutNotificationActionExtra.show()
                    binding.tvNotificationActionExtra.text = "Instagram ID"
                }
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }


    private suspend fun findContactsName(text: CharSequence): List<ContactModel> {
        val contact = Contacts(this@AddEventsActivity)
            .broadQuery()
            .wherePartiallyMatches("$text")
            .findAsync(Dispatchers.IO).await()
        return contact.map {
            ContactModel(
                it.displayNamePrimary ?: it.displayNameAlt ?: "",
                it.phoneList()[0].normalizedNumber ?: ""
            )
        }

        //return contact.map { it.displayNamePrimary ?: it.displayNameAlt ?: "" }
    }

    override fun onItemClick(eventsModel: ContactModel) {
        binding.etNotificationActionExtra.setText(eventsModel.name)
        whatsappNum = eventsModel.name
    }
}
