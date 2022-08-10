package com.app.remindme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.remindme.data.model.EventsModel
import com.app.remindme.databinding.ItemEventsBinding
import com.app.remindme.utils.logThis

class EventsAdapter(val listener: ItemClickListener) :
    RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {
    private val itemList = mutableListOf<EventsModel>()

    inner class MyViewHolder(val binding: ItemEventsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(itemList[position]) {
            logThis(this)
            holder.binding.tvTitle.text = title
            holder.binding.tvDesc.text = description
            holder.binding.tvEventEmojis.text = emoji
            if (emoji.length > 4) holder.binding.tvEventEmojis.isSelected = true
            holder.itemView.setOnClickListener {
                listener.onItemClick(this)
            }
        }
    }

    fun updateList(list: List<EventsModel>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

interface ItemClickListener {
    fun onItemClick(eventsModel: EventsModel)
}
