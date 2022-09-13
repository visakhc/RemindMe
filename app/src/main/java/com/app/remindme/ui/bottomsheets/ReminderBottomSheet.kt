package com.app.remindme.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.app.remindme.R
import com.app.remindme.databinding.BottomSheetReminderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReminderBottomSheet : BottomSheetDialogFragment() {
    private var binding: BottomSheetReminderBinding? = null
    private var dataList = listOf<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetReminderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initViews()
        handleEvents()
    }


    private fun init() {

    }

    private fun initViews() {
        dataList.forEachIndexed { index, bool ->
            when (index) {
                0 -> binding?.check1Day?.isChecked = bool
                1 -> binding?.check3Day?.isChecked = bool
                2 -> binding?.check7Day?.isChecked = bool
                3 -> binding?.check30Day?.isChecked = bool
            }
        }
    }

    fun showBottomSheet(supportFragmentManager: FragmentManager, data: List<Boolean>) {
        this.dataList = data
        show(supportFragmentManager, "ReminderBottomSheet")
    }

    fun setOnSaveListener(listener: OnSaveClickListener) {
        this.listener = listener
    }

    private lateinit var listener: OnSaveClickListener
    private fun handleEvents() {
        binding?.checkNever?.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                binding?.check1Day?.isChecked = false
                binding?.check3Day?.isChecked = false
                binding?.check7Day?.isChecked = false
                binding?.check30Day?.isChecked = false
            }
        }

        binding?.check1Day?.setOnCheckedChangeListener { _, b ->
            if (b) binding?.checkNever?.isChecked = false
        }
        binding?.check3Day?.setOnCheckedChangeListener { _, b ->
            if (b) binding?.checkNever?.isChecked = false
        }
        binding?.check7Day?.setOnCheckedChangeListener { _, b ->
            if (b) binding?.checkNever?.isChecked = false
        }
        binding?.check30Day?.setOnCheckedChangeListener { _, b ->
            if (b) binding?.checkNever?.isChecked = false
        }

        binding?.btSaveReminder?.setOnClickListener {
            listener.onSave(
                listOf(
                    binding?.check1Day?.isChecked!!,
                    binding?.check3Day?.isChecked!!,
                    binding?.check7Day?.isChecked!!,
                    binding?.check30Day?.isChecked!!
                )
            )
            dismiss()
        }
    }

    interface OnSaveClickListener {
        fun onSave(list: List<Boolean>)
    }
}