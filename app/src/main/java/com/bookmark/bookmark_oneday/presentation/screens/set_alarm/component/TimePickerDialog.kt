package com.bookmark.bookmark_oneday.presentation.screens.set_alarm.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogTimePickerBinding

class TimePickerDialog(
    private val setTime : (Int) -> Unit,
    private val currentTimeMinute : Int
) :DialogFragment() {
    private lateinit var binding : DialogTimePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogTimePickerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
        initTimePicker()
    }

    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSelect.setOnClickListener {
            setTime(binding.timePicker.hour * 60 + binding.timePicker.minute)
            dismiss()
        }
    }

    private fun initTimePicker() {
        binding.timePicker.hour = currentTimeMinute / 60
        binding.timePicker.minute = currentTimeMinute % 60
    }
}