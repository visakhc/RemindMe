package com.app.remindme.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.remindme.data.model.EventsModel
import com.app.remindme.databinding.ActivityAddEventsBinding
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.app.remindme.utils.shortToast

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
    }

    private fun handleEvents() {
        binding?.inclLayout?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.btSave?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val day = binding?.datePicker?.dayOfMonth!!
        val month = binding?.datePicker?.month!!
        val year = binding?.datePicker?.year!!
        val title = binding?.etTitle?.text.toString()
        val desc = binding?.etDesc?.text.toString()
        val emoji = binding?.etEmoji?.text.toString()

        viewModel.addEvent(EventsModel(day, month, year, title, desc, emoji))

        binding?.etEmoji?.text?.clear()
        binding?.etTitle?.text?.clear()
        binding?.etDesc?.text?.clear()

        shortToast("Event added!")
        super.onBackPressed()
    }
}