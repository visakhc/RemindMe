package com.app.remindme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.remindme.database.NoteSaveViewModel
import com.app.remindme.database.Notes
import com.app.remindme.databinding.ActivityAddEventsBinding
import com.google.android.material.snackbar.Snackbar

class AddEvents : AppCompatActivity() {
    private var binding: ActivityAddEventsBinding? = null
    private lateinit var notesSaveViewModel: NoteSaveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        notesSaveViewModel = ViewModelProvider(this).get(NoteSaveViewModel::class.java)

        /*   notesSaveViewModel = ViewModelProvider(this).get(NoteSaveViewModel::class.java)

           val ll = Notes(986565,658745,"tessssst","xxxxxxxxxxxxxxx")
           notesSaveViewModel.insertNote(ll)

           val intent = Intent(this@AddEvents, MainaaaaActivity::class.java)
           startActivity(intent)*/
       init()
    }

    private fun init() {
        binding?.inclLayout?.tvTitle?.text = "Add Events"
        binding?.inclLayout?.ivSettings?.visibility = View.GONE
//TODO---> Event Type Drop Down Items here
        val items = listOf("Birthday", "Anniversary", "A Loss")
        val adapter = ArrayAdapter(this@AddEvents, R.layout.list_item, items)
        binding?.tvEventType?.setAdapter(adapter)

        handleEvents()

    }

    private fun handleEvents() {
        binding?.inclLayout?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.btSave?.setOnClickListener {
            val title = binding?.etTitle?.text.toString()
            val desc = binding?.etDesc?.text.toString()
            val eventType = binding?.tvEventType?.text.toString()
            if (title.isEmpty() || desc.isEmpty() || eventType.isEmpty()){
            Snackbar.make(this@AddEvents,it,"Add All Details !!",Snackbar.LENGTH_SHORT).show()
            }else{
                val saveData = Notes(986565,658745,title,desc)
                notesSaveViewModel.insertNote(ll)            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}