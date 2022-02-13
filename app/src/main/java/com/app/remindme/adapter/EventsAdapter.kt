package com.app.remindme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.remindme.databinding.ItemEventsBinding
import com.app.remindme.model.EventsModel

class EventsAdapter(private val itemList: List<EventsModel>, context: Context) :
    RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {
    val ctxt = context

    inner class MyViewHolder(val binding: ItemEventsBinding)
        : RecyclerView.ViewHolder(binding.root)  {
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
            holder.binding.tvTitle.text = title
            holder.binding.tvDesc.text = description

            holder.binding.root.setOnClickListener {
                Toast.makeText(ctxt, (position + 1).toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}