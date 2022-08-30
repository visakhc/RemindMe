package com.app.remindme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.remindme.data.model.ContactModel
import com.app.remindme.databinding.ItemContactBinding

class ContactsAdapter(private val listener: ContactItemClickListener) :
    RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {
    private val itemList = mutableListOf<ContactModel>()

    inner class MyViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(itemList[position]) {
            holder.binding.tvName.text = name
            holder.binding.tvNumber.text = number
            holder.itemView.setOnClickListener {
                listener.onItemClick(this)
            }
        }
    }

    fun updateList(list: List<ContactModel>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface ContactItemClickListener {
        fun onItemClick(eventsModel: ContactModel)
    }
}
