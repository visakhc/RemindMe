package com.app.remindme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.sba.notes.database.Notes
import com.sba.notes.database.NotesRoomDatabase
import com.sba.notes.database.NotesViewModel
import com.sba.notes.databinding.ActivityMainBinding

class MainaaaaActivity : AppCompatActivity() {
    private lateinit var notesViewModel: NotesViewModel
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        gg()

        binding?.bt?.setOnClickListener {
            EditNoteFragment().show(supportFragmentManager, "dsd")
        }

    }

    private fun gg() {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        notesViewModel.allNotes.observe(this, androidx.lifecycle.Observer { notes ->
            Toast.makeText(this, notes.toString(), Toast.LENGTH_SHORT).show()
            binding?.tv?.text = notes.elementAt(1).title.toString()
//            fbind?.textView2?.text = notes.toString()
        })
    }
}

