package com.app.remindme.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.remindme.data.model.EventsModel
import com.app.remindme.databinding.ActivityAddEventsBinding
import com.app.remindme.ui.viewmodel.EventsViewModel

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
        binding?.inclLayout?.ivSettings?.visibility = View.GONE
    }

    private fun handleEvents() {
        binding?.inclLayout?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.btSave?.setOnClickListener {
            val title = binding?.etTitle?.text.toString()
            val desc = binding?.etDesc?.text.toString()
            viewModel.addEvent(EventsModel(title, desc))
        }
    }

}