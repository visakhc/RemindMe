package com.app.remindme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.remindme.databinding.ActivityaaaaMainBinding
import com.app.remindme.database.NotesViewModel

class MainaaaaActivity : AppCompatActivity() {
    private lateinit var notesViewModel: NotesViewModel
    private var binding: ActivityaaaaMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityaaaaMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        gg()

        binding?.bt?.setOnClickListener {
            EditNoteFragment().show(supportFragmentManager, "dsd")
        }

    }

    private fun gg() {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        notesViewModel.allNotes.observe(this, androidx.lifecycle.Observer { notes ->
           // binding?.tv?.text = notes.elementAt(1).title.toString()
            binding?.tv?.text = notes.toString()
//            fbind?.textView2?.text = notes.toString()
        })
    }
}

