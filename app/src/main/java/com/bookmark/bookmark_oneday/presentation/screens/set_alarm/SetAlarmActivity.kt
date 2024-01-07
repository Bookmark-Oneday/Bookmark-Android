package com.bookmark.bookmark_oneday.presentation.screens.set_alarm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.databinding.ActivitySetAlarmBinding
import com.bookmark.bookmark_oneday.presentation.base.ViewBindingActivity
import com.bookmark.bookmark_oneday.presentation.base.dialog.TwoButtonDialog
import com.bookmark.bookmark_oneday.presentation.screens.set_alarm.component.TimePickerDialog
import com.bookmark.bookmark_oneday.presentation.util.EXACT_ALARM_PERMISSION
import com.bookmark.bookmark_oneday.presentation.util.POST_NOTIFICATIONS_33
import com.bookmark.bookmark_oneday.presentation.util.checkExactAlarmAvailable
import com.bookmark.bookmark_oneday.presentation.util.checkPostNotificationAvailable
import com.bookmark.bookmark_oneday.presentation.util.collectLatestInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetAlarmActivity : ViewBindingActivity<ActivitySetAlarmBinding>(ActivitySetAlarmBinding::inflate) {
    private val viewModel : SetAlarmViewModel by viewModels()

    private val requestExactNotificationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionGrantedMap ->
        permissionGrantedMap.values
            .all { it }
            .run {
                if (this@run) {
                    viewModel.setUseSwitch(true)
                } else {
                    viewModel.setUseSwitch(false)
                    callRequirePermissionDialog()
                }
            }
    }

    private fun callRequirePermissionDialog() {
        val dialog = TwoButtonDialog(
            title = getString(R.string.label_notify_require_permissions_for_alarm),
            caption = getString(R.string.caption_notify_require_permissions_for_alarm),
            leftText = getString(R.string.cancel),
            rightText = getString(R.string.moving),
            onLeftButtonClick = {},
            onRightButtonClick = {
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {
                    startActivity(this)
                }
            },
            accentLeft = false
        )

        dialog.show(supportFragmentManager, "requirePermissionDialog")
    }

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
            if (isChecked &&
                !(checkExactAlarmAvailable(baseContext) && checkPostNotificationAvailable(baseContext))
            ) {
                listOfNotNull(EXACT_ALARM_PERMISSION, POST_NOTIFICATIONS_33)
                    .run {
                        if (isEmpty()) return@run
                        requestExactNotificationPermissions.launch(toTypedArray())
                    }

                return@setOnCheckedChangeListener
            }
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