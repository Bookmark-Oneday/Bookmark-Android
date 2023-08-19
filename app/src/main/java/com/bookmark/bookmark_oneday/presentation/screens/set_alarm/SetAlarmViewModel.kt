package com.bookmark.bookmark_oneday.presentation.screens.set_alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.presentation.alarm.AlarmManager
import com.bookmark.bookmark_oneday.domain.appinfo.usecase.UseCaseGetAlarmInfo
import com.bookmark.bookmark_oneday.domain.appinfo.usecase.UseCaseSetAlarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetAlarmViewModel @Inject constructor(
    private val alarmManager: AlarmManager,
    private val useCaseSetAlarm: UseCaseSetAlarm,
    useCaseGetAlarmInfo: UseCaseGetAlarmInfo
) : ViewModel() {

    private val _useAlarm = MutableStateFlow(false)
    val useAlarm = _useAlarm.asStateFlow()

    private val _alarmTime = MutableStateFlow(20 * 60)
    val alarmTime = _alarmTime.asStateFlow()

    private val _saveAlarmSettingResult = MutableSharedFlow<Boolean>()
    val saveAlarmSettingResult = _saveAlarmSettingResult.asSharedFlow()

    init {
        viewModelScope.launch {
            useCaseGetAlarmInfo().collectLatest {
                _useAlarm.value = it.alarmOn
                _alarmTime.value = (it.hour * 60 + it.minute)
            }
        }
    }

    fun setUseSwitch(use : Boolean) {
        _useAlarm.value = use
    }

    fun setAlarmTime(timeMinute : Int) {
        _alarmTime.value = timeMinute
    }
    
    fun saveAlarmSetting() {
        viewModelScope.launch {
            val hour = alarmTime.value / 60
            val minute = alarmTime.value % 60

            useCaseSetAlarm(
                alarmOn = useAlarm.value,
                hour = hour,
                minute = minute
            )

            if (useAlarm.value) {
                alarmManager.setAlarmOn(
                    hour = hour,
                    minute = minute
                )
            } else {
                alarmManager.setAlarmOff()
            }

            _saveAlarmSettingResult.emit(true)
        }
    }
}