package com.app.remindme.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.remindme.adapter.CalendarAdapter
import com.app.remindme.bottomsheets.EventsBottomSheet
import com.app.remindme.data.model.CalenderModel
import com.app.remindme.databinding.ActivityMainBinding
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.app.remindme.utils.logThis
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), CalendarAdapter.OnEachListener {

    private var binding: ActivityMainBinding? = null
    private var modelList = mutableListOf<CalenderModel>()
    private val mCalendar = Calendar.getInstance()
    private var mMonth = mCalendar.get(Calendar.MONTH)

    private lateinit var viewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]


        binding?.inclLayout?.tvTitle?.apply {
            text = SimpleDateFormat("EEEE").format(mCalendar.time)
        }
        binding?.inclLayout?.ivSettings?.visibility = View.GONE
        init()
    }

    private fun init() {
        setRecyclerview()
        setToolbarMonth()
        roomDB()
        handleEvents()
    }

    private fun roomDB() {
        viewModel.readAllData.observe(this@MainActivity) {
            logThis(it)
        }
    }


    private fun setToolbarMonth() {

/*        with(binding?.inclLayout?.valuePickerView) {
            val pickerItems = generateTeamPickerItems()
            this?.items = pickerItems
            this?.setSelectedItem(pickerItems[mMonth])
            this?.onItemSelectedListener = ValuePickerView.OnItemSelectedListener { item ->
                val pos = this?.selectedItem?.id
                if (pos != null) {
                    mMonth = pos
                    setRecyclerview()
                }
            }
        }*/
    }


    private fun handleEvents() {
        binding?.btAddEvent?.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEvents::class.java)
            startActivity(intent)
        }
    }


    private fun setRecyclerview() {
        val flexboxLayoutManager = FlexboxLayoutManager(this@MainActivity)
        flexboxLayoutManager.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.SPACE_EVENLY
        }
        val data = setData(mCalendar)
        val recyclerAdapter = CalendarAdapter(data, mMonth, this)

        binding?.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = flexboxLayoutManager
            adapter = recyclerAdapter
        }


    }

    private fun setData(calendar: Calendar): MutableList<CalenderModel> {
        /* val year = calendar.get(Calendar.YEAR).toString()
         val month = calendar.get(Calendar.MONTH).toString()
         val date = calendar.get(Calendar.DATE).toString()
         val monthinWords = SimpleDateFormat("MMMM").format(calendar.time)*/

        modelList.clear()

        calendar.set(calendar.get(Calendar.YEAR), mMonth, 5)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxDay) {
            calendar.set(calendar.get(Calendar.YEAR), mMonth, i)
            val day = (SimpleDateFormat("EEEE").format(calendar.time))
            modelList.add(CalenderModel(i.toString(), day))
        }
        return modelList
    }


/*    private fun generateTeamPickerItems(): List<Item> {
        return monthList.values().map {
            PickerItem(
                id = it.ordinal,
                title = it.longName,
                payload = it
            )
        }
    }*/

    override fun OnEachClick(position: Int) {
        EventsBottomSheet(position, mMonth).show(supportFragmentManager, "events")
    }
}

internal enum class monthList(val longName: String) {
    January(longName = "January"),
    February(longName = "February"),
    March(longName = "March"),
    April(longName = "April"),
    May(longName = "May"),
    June(longName = "June"),
    July(longName = "July"),
    August(longName = "August"),
    September(longName = "September"),
    October(longName = "October"),
    November(longName = "November"),
    December(longName = "December")
}