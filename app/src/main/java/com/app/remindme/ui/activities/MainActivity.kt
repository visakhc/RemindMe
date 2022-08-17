package com.app.remindme.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.app.remindme.R
import com.app.remindme.adapter.CalendarAdapter
import com.app.remindme.data.model.CalendarModel
import com.app.remindme.databinding.ActivityMainBinding
import com.app.remindme.ui.bottomsheets.EventsBottomSheet
import com.app.remindme.ui.viewmodel.EventsViewModel
import com.app.remindme.utils.USERDATA
import com.app.remindme.utils.USERDATA.thisMonth
import com.app.remindme.utils.getDayFormatted
import com.app.remindme.utils.hide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("ClickableViewAccessibility")
class MainActivity : AppCompatActivity(), CalendarAdapter.OnClickListener {

    private var calendarList = emptyList<CalendarModel>()
    private var mMonth: Int = -1
    private var mYear: Int = -1
    private var binding: ActivityMainBinding? = null
    private val mAdapter by lazy { CalendarAdapter(this) }
    private val flexboxLayout by lazy { FlexboxLayoutManager(this@MainActivity) }

    private lateinit var viewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        // viewModel.deleteAllCalendarData()
        initViews()
        setCalendarView()
        handleEvents()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        mMonth = thisMonth
        mYear = USERDATA.thisYear
    }

    private fun initViews() {
        binding?.btAddEvent?.hide() //for testing
        //  binding?.tvDay?.text = getDayFormatted("EEEE")
        //  binding?.tvMonth?.text = getDayFormatted("MMMM")
        flexboxLayout.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.SPACE_EVENLY
        }
        binding?.recyclerView?.layoutManager = flexboxLayout
        binding?.recyclerView?.adapter = mAdapter
/*      val months = resources.getStringArray(R.array.months_array)
        binding?.monthPicker?.apply {
            minValue = 0
            maxValue = months.lastIndex
            displayedValues = months
            value = mMonth
            wrapSelectorWheel = false

            setOnValueChangedListener { _, _, pos ->
                mMonth = pos
                binding?.tvMonth?.text = months[pos]
                setCalendarView()
            }
          for (i in data.indices) {
                  if (selectedUnit == data[i]?.name) {
                      selectedUnitId = data[i]?.id.toString()
                      break
                  }
              }
        }*/
    }

    private fun handleEvents() {
        binding?.ivSettings?.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEvents::class.java)
            startActivity(intent)
        }
        binding?.btAddEvent?.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEvents::class.java)
            startActivity(intent)
        }
        binding?.monthPicker?.setOnClickListener {
            showMenu(it, R.menu.calendar_menu)
        }

        /*  binding?.tvMonth?.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
              override fun onSwipeTop() {
                  swipedToCal(mMonth--)

              }

              override fun onSwipeRight() {
                  swipedToCal(mMonth--)

              }

              override fun onSwipeLeft() {
                  swipedToCal(mMonth++)

              }

              override fun onSwipeBottom() {
                  swipedToCal(mMonth++)
              }
          })*/

    }

    private fun setCalendarView() {
        val cal = Calendar.getInstance()
        cal.set(mYear, mMonth, 4)
        binding?.tvMonth?.text = getDayFormatted("MMMM", cal)
        binding?.tvDay?.text = if (mMonth == thisMonth) getDayFormatted("EEEE") else ""
        lifecycle.coroutineScope.launch { //todo add splash screen to get time
            val localCalendarData = viewModel.getCalendarData(mMonth, mYear)
            calendarList = localCalendarData.ifEmpty { calendarBuilder(mMonth, mYear) }
            mAdapter.updateList(mMonth, calendarList)
        }
        viewModel.findEventDayInMonth(mMonth, mYear).observe(this) {
            it?.forEach { item ->
                mAdapter.updateListWithEvents(mMonth, item.day, item.emoji)
            }
        }
    }


    private fun calendarBuilder(month: Int, year: Int): MutableList<CalendarModel> {
        val dayList = mutableListOf<CalendarModel>()
        val cal = Calendar.getInstance()
        cal.set(year, month, 1)
        val maxDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (date in 1..maxDayOfMonth) {
            cal.set(year, month, date)
            val dayInWords = getDayFormatted("EEEE", cal)
            dayList.add(
                CalendarModel(
                    date,
                    dayInWords,
                    month,
                    year
                )
            ) //try to send list as whole todo
        }
        viewModel.addCalendarData(dayList)
        return dayList
    }


    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        //todo change this to more lite way
        val popup = PopupMenu(this@MainActivity, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {
            val month = when (it.itemId) {
                R.id.january -> 0
                R.id.february -> 1
                R.id.march -> 2
                R.id.april -> 3
                R.id.may -> 4
                R.id.june -> 5
                R.id.july -> 6
                R.id.august -> 7
                R.id.september -> 8
                R.id.october -> 9
                R.id.november -> 10
                R.id.december -> 11
                else -> {
                    -1
                }
            }
            if (month != -1) {
                mMonth = month
                setCalendarView()
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    override fun onItemClick(item: CalendarModel) {
        val eBSheet = EventsBottomSheet()
        eBSheet.arguments = Bundle().apply {
            putInt("day", item.date)
            putInt("month", mMonth)
            putInt("year", mYear)
        }
        eBSheet.show(supportFragmentManager, "events")
    }
}
