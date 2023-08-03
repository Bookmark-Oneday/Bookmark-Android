package com.bookmark.bookmark_oneday.presentation.screens.set_alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetAlarmViewModel @Inject constructor(

) : ViewModel() {
    private val _useAlarm = MutableStateFlow(false)
    val useAlarm = _useAlarm.asStateFlow()

    private val _alarmTime = MutableStateFlow(20 * 60)
    val alarmTime = _alarmTime.asStateFlow()

    private val _saveAlarmSettingResult = MutableSharedFlow<Boolean>()
    val saveAlarmSettingResult = _saveAlarmSettingResult.asSharedFlow()

    fun setUseSwitch(use : Boolean) {
        _useAlarm.value = use
    }

    fun setAlarmTime(timeMinute : Int) {
        _alarmTime.value = timeMinute
    }
    
    fun saveAlarmSetting() {
        viewModelScope.launch {
            _saveAlarmSettingResult.emit(true)
        }
    }
}