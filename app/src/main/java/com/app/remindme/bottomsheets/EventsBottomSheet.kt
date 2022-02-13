package com.app.remindme.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.remindme.R
import com.app.remindme.adapter.EventsAdapter
import com.app.remindme.databinding.HomeAlertLayoutBinding
import com.app.remindme.model.EventsModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class EventsBottomSheet(date: Int) : BottomSheetDialogFragment() {
    private var binding: HomeAlertLayoutBinding? = null
    private val mDate = date+1
    private var meventsArray = mutableListOf(EventsModel("rdtcfygvbh","lkkk"))


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeAlertLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }


    fun init() {
        val cal = Calendar.getInstance()
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), mDate)
        binding?.tvDate?.text = SimpleDateFormat("MMM, dd").format(cal.time)


        val recyclerAdapter = EventsAdapter(meventsArray, requireContext())
        binding?.rvAlert?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }

    }
}