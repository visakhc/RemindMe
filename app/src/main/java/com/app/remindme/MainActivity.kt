package com.app.remindme

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.remindme.adapter.CalendarAdapter
import com.app.remindme.bottomsheets.EventsBottomSheet
import com.app.remindme.databinding.ActivityMainBinding
import com.app.remindme.model.CalenderModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), CalendarAdapter.OnEachListener {

    private var binding: ActivityMainBinding? = null
    private var arrayDatewe = mutableListOf<CalenderModel>()
    private val mCalendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.inclLayout?.tvTitle?.apply {
            text = SimpleDateFormat("EEEE").format(mCalendar.time)
        }
        binding?.inclLayout?.ivBack?.visibility = View.GONE
        binding?.inclLayout?.ivSettings?.visibility = View.GONE
        init()
    }

    private fun init() {
        setRecyclerview()
        handleEvents()
    }


    private fun showDialog() {


        /*   val UPI = "upi://pay?pa=8547917584@okbizaxis&pn=Prabha Pooja Store&mc=5943&aid=uGICAgIDtrYDsWQ&tr=BCR2DN6T2WD2FQTC"
           val intent = Intent()
           intent.action = Intent.ACTION_VIEW
           intent.data = Uri.parse(UPI)
           val chooser = Intent.createChooser(intent, "Pay with...")
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
               startActivityForResult(chooser, 1, null)
           }
           startActivityForResult(chooser,1,null)
*/
    }


    private fun handleEvents() {
        binding?.btAddEvent?.setOnClickListener{
            val intent = Intent(this@MainActivity,AddEvents::class.java)
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
        val recyclerAdapter = CalendarAdapter(data, this)

        binding?.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = flexboxLayoutManager
            adapter = recyclerAdapter
        }
    }

    private fun setData(calendar: Calendar): MutableList<CalenderModel> {
//        val year = calendar.get(Calendar.YEAR).toString()
//        val month = calendar.get(Calendar.MONTH).toString()
//        val date = calendar.get(Calendar.DATE).toString()
//        val monthinWords = SimpleDateFormat("MMMM").format(calendar.time)

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDay) {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), i)
            val day = (SimpleDateFormat("EEEE").format(calendar.time))
            arrayDatewe.add(CalenderModel(i.toString(), day))
        }
        return arrayDatewe
    }

    override fun OnEachClick(position: Int) {
        EventsBottomSheet(position).show(supportFragmentManager, "events")
    }
}