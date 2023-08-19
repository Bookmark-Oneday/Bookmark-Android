package com.bookmark.bookmark_oneday.presentation.screens.set_alarm

import android.os.Bundle
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivitySetAlarmBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.screens.set_alarm.component.TimePickerDialog
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetAlarmActivity : ViewBindingActivity<ActivitySetAlarmBinding>(ActivitySetAlarmBinding::inflate) {
    private val viewModel : SetAlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initButton()
        initSwitch()
        initObserver()
    }

    private fun initButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.clBtnSetAlarmTime.setOnClickListener {
            val dialog = TimePickerDialog(viewModel::setAlarmTime, viewModel.alarmTime.value)
            dialog.show(supportFragmentManager, "alarmTimePicker")
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.saveAlarmSetting()
        }
    }

    private fun initSwitch() {
        binding.switchUseAlarm.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setUseSwitch(isChecked)
        }
    }

    private fun initObserver() {
        viewModel.useAlarm.collectLatestInLifecycle(this) { useAlarm ->
            setAlarmBtnEnabled(useAlarm)
            binding.switchUseAlarm.isChecked = useAlarm
        }

        viewModel.alarmTime.collectLatestInLifecycle(this) { alarmTimeMinute ->
            setTimeTextView(alarmTimeMinute)
        }

        viewModel.saveAlarmSettingResult.collectLatestInLifecycle(this) { alarmSaveSuccess ->
            if (alarmSaveSuccess) {
                finish()
            }
        }
    }

    private fun setAlarmBtnEnabled(enabled : Boolean) {
        binding.clBtnSetAlarmTime.isClickable = enabled
        binding.labelAlarmTime.isEnabled = enabled
        binding.imgArrow.isEnabled = enabled
    }

    private fun setTimeTextView(timeMinute : Int) {
        val hour = timeMinute / 60
        val minute = timeMinute % 60
        val text = if (hour == 0) {
            getString(R.string.label_time_of_day_night_format, 12, minute)
        } else if (hour == 12) {
            getString(R.string.label_time_of_day_afternoon_format, 12, minute)
        } else if (hour > 12) {
            getString(R.string.label_time_of_day_pm_format, hour - 12, minute)
        } else {
            getString(R.string.label_time_of_day_am_format, hour, minute)
        }
        binding.labelAlarmTime.text = text
    }
}