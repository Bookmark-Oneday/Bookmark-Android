package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bookmark.bookmark_oneday.databinding.DialogCalendarMonthPickerBinding

class CalendarMonthPickerDialog(
    private val onClick : (Int, Int) -> Unit,
    private val currentYear : Int,
    private val currentMonth : Int
) : DialogFragment() {
    private lateinit var binding : DialogCalendarMonthPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogCalendarMonthPickerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNumberPicker()
        initButton()
    }

    private fun initNumberPicker() {
        binding.pickerCalendarMonth.minValue = 1
        binding.pickerCalendarMonth.maxValue = 12
        binding.pickerCalendarMonth.value = currentMonth

        binding.pickerCalendarYear.minValue = 2020
        binding.pickerCalendarYear.maxValue = 2100
        binding.pickerCalendarYear.value = currentYear
    }

    private fun initButton() {
        binding.btnCalendarCancel.setOnClickListener {
            dismiss()
        }

        binding.btnCalendarSelect.setOnClickListener {
            val year = binding.pickerCalendarYear.value
            val month = binding.pickerCalendarMonth.value

            onClick(year, month)
            dismiss()
        }
    }
}