package com.bookmark.bookmark_oneday.presentation.screens.modify_reading_time

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetUser
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseSetUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifyReadingTimeViewModel @Inject constructor(
    private val useCaseSetUser: UseCaseSetUser,
    useCaseGetUser: UseCaseGetUser
) : ViewModel() {
    private val _inputReadingTime = MutableStateFlow(0)
    val inputReadingTime = _inputReadingTime.asStateFlow()

    private val _modifyReadingTimeResult = MutableSharedFlow<Boolean>()
    val modifyReadingTimeResult = _modifyReadingTimeResult.asSharedFlow()

    init {
        viewModelScope.launch {
            useCaseGetUser.getGoalReadingTime().collectLatest {
                _inputReadingTime.value = it
            }
        }
    }

    fun setInputReadingTime(minute : Int) {
        _inputReadingTime.value = minute
    }

    fun modifyGoalReadingTime() {
        viewModelScope.launch {
            useCaseSetUser.setReadingTime(inputReadingTime.value)
            _modifyReadingTimeResult.emit(true)
        }
    }

}