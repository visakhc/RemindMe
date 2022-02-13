package com.app.remindme

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.remindme.databinding.ActivityAddEventsBinding

class AddEvents : AppCompatActivity() {
    private var binding: ActivityAddEventsBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddEventsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
    }
    private fun init(){
        binding?.inclLayout?.tvTitle?.text = "Add Events"
        binding?.inclLayout?.ivSettings?.visibility = View.GONE

        handleEvents()

    }

    private fun handleEvents() {
        binding?.inclLayout?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.btSave?.setOnClickListener {
            val title = binding?.etTitle?.text
            val desc = binding?.etDesc?.text
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}