package com.app.remindme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.remindme.R
import com.app.remindme.data.model.CalenderModel
import java.util.*


class CalendarAdapter(
    private val itemList: List<CalenderModel>,
    private  val mMonth: Int,
    private var mOnEachListener: OnEachListener,
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    val today = Calendar.getInstance().get(Calendar.DATE)
    val thisMonth = Calendar.getInstance().get(Calendar.MONTH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_calender, parent, false)
        return ViewHolder(view, mOnEachListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View, OnEachListener: OnEachListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mName: TextView
        var mNum: TextView
        var d: CardView
        var OnEachListener: OnEachListener
        override fun onClick(v: View) {
            OnEachListener.OnEachClick(adapterPosition)
        }

        init {
            mName = itemView.findViewById(R.id.tv_date)
            mNum = itemView.findViewById(R.id.tv_day)
            d = itemView.findViewById(R.id.cardView)
            this.OnEachListener = OnEachListener
            itemView.setOnClickListener(this)
        }
    }

    interface OnEachListener {
        fun OnEachClick(position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(itemList[position]) {
            holder.mName.text = date
            holder.mNum.text = day
        }

        if ((position+1) == today && thisMonth == mMonth ) {
            holder.d.setBackgroundResource(R.drawable.shape_border)
        }
    }
}
