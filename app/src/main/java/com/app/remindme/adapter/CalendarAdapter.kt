package com.app.remindme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.remindme.R
import com.app.remindme.data.model.CalendarModel
import com.app.remindme.databinding.ItemCalenderBinding
import com.app.remindme.utils.USERDATA.thisDay
import com.app.remindme.utils.USERDATA.thisMonth


class CalendarAdapter(private var listener: OnClickListener) :
    RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {
    private var mMonth: Int = -1
    private var localList: MutableList<CalendarModel> = mutableListOf()

    inner class MyViewHolder(val binding: ItemCalenderBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarAdapter.MyViewHolder {
        return MyViewHolder(
            ItemCalenderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CalendarAdapter.MyViewHolder, position: Int) {
        with(localList[position]) {
            holder.binding.tvDate.text = date.toString()
            holder.binding.tvDay.text = day
            holder.binding.tvEventEmojis.text = emoji
            if (emoji.length > 4) holder.binding.tvEventEmojis.isSelected = true
            if ((position + 1) == thisDay && mMonth == thisMonth) {
                holder.binding.cardView.setBackgroundResource(R.drawable.shape_border)
            }
            holder.itemView.setOnClickListener {
                listener.onItemClick(this)
            }

        }
    }

    override fun getItemCount(): Int {
        return localList.size
    }

    fun updateList(month: Int, list: List<CalendarModel>) {
        mMonth = month
        localList.clear()
        localList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateListWithEvents(month: Int, pos: Int, emoji: String) {
        mMonth = month
        localList[pos - 1].emoji = emoji
        notifyItemChanged(pos - 1)
    }

    interface OnClickListener {
        fun onItemClick(item: CalendarModel)
    }
}
