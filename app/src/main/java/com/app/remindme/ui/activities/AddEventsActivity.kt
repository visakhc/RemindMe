package com.app.remindme.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextWatcher
import android.view.View
import android.widget.ScrollView
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
import com.app.remindme.services.pendingIntentFlags
import com.app.remindme.ui.bottomsheets.ReminderBottomSheet
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.app.remindme.utils.*
import com.app.remindme.utils.USERDATA.NOTIFICATION_CHANNEL_ID
import contacts.async.findAsync
import contacts.core.Contacts
import contacts.core.util.phoneList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class AddEventsActivity : AppCompatActivity(), ContactsAdapter.ContactItemClickListener,
    ReminderBottomSheet.OnSaveClickListener {
    private lateinit var textWatcher: TextWatcher
    private lateinit var viewModel: EventsViewModel
    private var whatsappNum: String? = null
    private val contactsAdapter by lazy { ContactsAdapter(this@AddEventsActivity) }
    private var hasRecyclerViewFocused = false // to focus recycler view when keyboard is open
    private var eventReminderList = listOf<Boolean>()
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
        binding.scrollView.isSmoothScrollingEnabled = true

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
                shortToast("Title is required")
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

        binding.tvRepeatStatus.setOnClickListener {
            val reminderBottomSheet = ReminderBottomSheet()
            reminderBottomSheet.showBottomSheet(supportFragmentManager, eventReminderList)
            reminderBottomSheet.setOnSaveListener(this)
        }

        textWatcher = binding.etNotificationActionExtra.doOnTextChanged { txt, _, _, _ ->
            whatsappNum = null
            if (txt != null && txt.isNotBlank() && txt.length > 3 && binding.tvNotificationAction.text == "Whatsapp") {
                val text = txt
                lifecycleScope.launch {
                    val nameList = findContactsName(text)
                    binding.rvContacts.show()
//                    if (nameList.isNotEmpty()) binding.rvContacts.show() else binding.rvContacts.hide()
                    val typedNumber =
                        if ("${text.subSequence(0, 3)}" == "+91") "$text" else "+91$text"
                    contactsAdapter.updateList(typedNumber, nameList)
                    if (!hasRecyclerViewFocused) {
                        binding.scrollView.arrowScroll(ScrollView.FOCUS_DOWN)
                        binding.etNotificationActionExtra.requestFocus()
                        hasRecyclerViewFocused = true
                    }
                }
            } else {
                binding.rvContacts.hide()
                hasRecyclerViewFocused = false
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
        if (title.isBlank() && desc.isBlank() && emoji.isBlank()) super.onBackPressed()
        else if (title.isNotBlank()) {
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
            var notificationActionMsg = ""
            var notificationExtraData = ""

            when (notificationAction) {
                "Whatsapp" -> {
                    if (whatsappNum != null) {
                        notificationExtraData = whatsappNum!!
                        notificationActionMsg = binding.etMessage.text.toString()
                    } else {
                        shortToast("Please enter a valid number or name")
                        binding.etNotificationActionExtra.error =
                            "Please enter a valid number or name"
                        return
                    }
                }
                "Instagram" -> {
                    val insta = binding.etNotificationActionExtra.text.toString()
                    if (insta.isNotBlank()) {
                        notificationExtraData = insta
                    } else {
                        shortToast("Please enter an instagram username")
                        binding.etNotificationActionExtra.error =
                            "Please enter an instagram username"
                        return
                    }
                }
            }

            if (intent.action == "edit" && id != -11) { //todo fix onbackpressed with editing data
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
                    //create or edit todo event reminder intent for this with update an already existing
                    shortToast("Event updated")
                    super.onBackPressed()
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
                shortToast("Event added")
                super.onBackPressed()
            }

        } else {
            binding.etTitle.error = "Title is required"
            shortToast("Title is required")
        }


    }

    private fun createEventReminder(notificationModel: NotificationModel) {
        val args = Bundle()
        args.putSerializable("notificationModel", notificationModel)
        val intent = Intent(this@AddEventsActivity, NotifyEventService::class.java)
        intent.putExtra("data", args)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            this@AddEventsActivity,
            0,
            intent,
            pendingIntentFlags
        )
        val calendar = Calendar.getInstance()
        calendar.set(
            notificationModel.eventsModel.year,
            notificationModel.eventsModel.month,
            notificationModel.eventsModel.day,
            notificationModel.hour,
            notificationModel.minute,
            0
        )
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val test: String = sdf.format(calendar.time)
        logThis("Event Set for $test")
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        if (eventReminderList.isNotEmpty()) {
            eventReminderList.forEachIndexed { index, bool ->
                when (index) {
                    0 -> {
                        if (bool) { //sets alarm for 1 day before
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis - 1000 * 60 * 60 * 24 * 1,
                                pendingIntent
                            )
                        }
                    }
                    1 -> { //sets alarm 3 days before
                        if (bool) {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis - 1000 * 60 * 60 * 24 * 3,
                                pendingIntent
                            )
                        }

                    }
                    2 -> {//sets alarm 7 days before
                        if (bool) {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis - 1000 * 60 * 60 * 24 * 7,
                                pendingIntent
                            )
                        }

                    }
                    3 -> {//sets alarm 28 days before
                        if (bool) {
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis - 1000 * 60 * 60 * 24 * 28L,
                                pendingIntent
                            )
                        }


                    }
                }
            }
        }
    }


    private fun showPopupMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this@AddEventsActivity, view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {
            binding.tvNotificationAction.text = it.title
            when (it.title) {
                "Default" -> {
                    binding.layoutNotificationActionExtra.hide()
                }
                "Whatsapp" -> {
                    binding.etMessage.show()
                    binding.layoutNotificationActionExtra.show()
                    binding.tvNotificationActionExtra.text = "Whatsapp Number"
                    binding.etNotificationActionExtra.hint = "name or number"
                }
                "Instagram" -> {
                    binding.etMessage.hide()
                    binding.layoutNotificationActionExtra.show()
                    binding.tvNotificationActionExtra.text = "Instagram ID"
                    binding.etNotificationActionExtra.hint = "username"
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
        binding.etNotificationActionExtra.removeTextChangedListener(textWatcher)
        binding.etNotificationActionExtra.setText(eventsModel.name)
        //next line checks iif the typed number is a mobile number or not with pattern
        whatsappNum = eventsModel.number
        binding.rvContacts.hide()
        binding.etMessage.requestFocus()
        binding.etNotificationActionExtra.addTextChangedListener(textWatcher)
    }

    override fun onSave(list: List<Boolean>) {
        eventReminderList = list
        var text = ""
        list.forEachIndexed { index, bool ->
            text += when (index) {
                0 -> if (bool) "1, " else ""
                1 -> if (bool) "3, " else ""
                2 -> if (bool) "7, " else ""
                3 -> if (bool) "28, " else ""
                else -> ""
            }
            if (index == list.lastIndex) {
                binding.tvRepeatStatus.text =
                    if (text.isNotBlank()) "${text}days before the event" else "Never"
            }
        }

    }
}
