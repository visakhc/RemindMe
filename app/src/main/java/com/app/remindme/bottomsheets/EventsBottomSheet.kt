package com.app.remindme.bottomsheets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.remindme.R
import com.app.remindme.adapter.EventsAdapter
import com.app.remindme.databinding.BottomSheetLayoutBinding
import com.app.remindme.ui.activities.AddEvents
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class EventsBottomSheet() : BottomSheetDialogFragment() {
    private var binding: BottomSheetLayoutBinding? = null

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        handleEvents()
    }


    private fun init() {
        val cal = Calendar.getInstance()
       /* cal.set(cal.get(Calendar.YEAR), mMonth, mDate + 1)
        binding?.tvDate?.text = SimpleDateFormat("MMM, dd").format(cal.time)


        val recyclerAdapter = EventsAdapter(meventsArray, requireContext())
        binding?.rvAlert?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }*/

    }

    private fun handleEvents() {
        binding?.ivAdd?.setOnClickListener {
            val intent = Intent(requireContext(), AddEvents::class.java)
            startActivity(intent)
        }
    }
}