package com.app.remindme.ui.bottomsheets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.remindme.R
import com.app.remindme.adapter.EventsAdapter
import com.app.remindme.data.model.EventsModel
import com.app.remindme.databinding.BottomSheetLayoutBinding
import com.app.remindme.ui.activities.AddEventsActivity
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.app.remindme.utils.hide
import com.app.remindme.utils.show
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EventsBottomSheet : BottomSheetDialogFragment(), EventsAdapter.ItemClickListener {
    private var binding: BottomSheetLayoutBinding? = null
    private lateinit var viewModel: EventsViewModel
    private var date = -1
    private var month = -1
    private var year = -1
    private val recyclerAdapter by lazy { EventsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initViews()
        getEvents()
        handleEvents()
    }


    private fun init() {
        arguments?.let {
            date = it.getInt("day")
            month = it.getInt("month")
            year = it.getInt("year")
        }
    }

    private fun initViews() {
//        binding?.tvDate?.text = "${date}/${month + 1}/${year}"
        binding?.rvAlert?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
    }

    private fun getEvents() {
        viewModel.findEvent(date, month, year).observe(this) {
            if (it.isNotEmpty()) {
                recyclerAdapter.updateList(it)
                binding?.tvNoEvents?.hide()
            } else binding?.tvNoEvents?.show()

        }
    }

    //todo add reminder for events
    private fun handleEvents() {
        binding?.ivAdd?.setOnClickListener {
            val intent = Intent(requireContext(), AddEventsActivity::class.java)
            intent.putExtra("day", date)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            startActivity(intent)
        }
    }

    override fun onItemClick(eventsModel: EventsModel) {
        val intent = Intent(requireContext(), AddEventsActivity::class.java)
        intent.action = "edit"
        intent.putExtra("day", date)
        intent.putExtra("month", month)
        intent.putExtra("year", year)
        intent.putExtra("title", eventsModel.title)
        intent.putExtra("description", eventsModel.description)
        intent.putExtra("emoji", eventsModel.emoji)
        intent.putExtra("id", eventsModel.id)
        startActivity(intent)
    }
}