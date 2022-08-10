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
import com.app.remindme.databinding.BottomSheetLayoutBinding
import com.app.remindme.ui.activities.AddEvents
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EventsBottomSheet : BottomSheetDialogFragment() {
    private var binding: BottomSheetLayoutBinding? = null
    private lateinit var viewModel: EventsViewModel
    private var date = -1
    private var month = -1
    private var year = -1
    private val recyclerAdapter by lazy { EventsAdapter() }

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
        binding?.tvDate?.text = "${date}/${month + 1}/${year}"
        binding?.rvAlert?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
    }

    private fun getEvents() {
        viewModel.findEvent(date, month, year).observe(this) {
            recyclerAdapter.updateList(it)
        }
    }

    private fun handleEvents() {
        binding?.ivAdd?.setOnClickListener {
            val intent = Intent(requireContext(), AddEvents::class.java)
            intent.putExtra("day", date)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            startActivity(intent)
        }
    }
}