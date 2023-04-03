package com.bookmark.bookmark_oneday.presentation.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetReadingHistory
import com.bookmark.bookmark_oneday.presentation.model.Timer
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.StopWatchState
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.TimerViewEvent
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.TimerViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimerViewModel @AssistedInject constructor(
    private val useCaseGetReadingHistory: UseCaseGetReadingHistory,
    @Assisted val bookId : String
) : ViewModel() {

    private val timer = Timer(viewModelScope, Dispatchers.Default, action = ::setStopWatchTime)

    private val events = Channel<TimerViewEvent>()
    val state : StateFlow<TimerViewState> = events.receiveAsFlow()
        .runningFold(TimerViewState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerViewState())

    private val _stopWatchState = MutableStateFlow(StopWatchState())
    val stopWatchState = _stopWatchState.asStateFlow()

    // 현재 total time 은 임시
    private var dailyReadingTime = 0
    private var addedTime = 0

    private fun setStopWatchTime(time : Int) {
        viewModelScope.launch {
            val appliedTimeSecond = time + addedTime
            _stopWatchState.value = StopWatchState(
                timeString = String.format("%02d:%02d", appliedTimeSecond / 60, appliedTimeSecond % 60),
                progress = appliedTimeSecond
            )
        }
    }

    fun tryGetReadingHistory() {
        viewModelScope.launch {
            events.send(TimerViewEvent.ApiResponseLoading)
            val response = useCaseGetReadingHistory(bookId)
            if (response is BaseResponse.Success) {
                events.send(TimerViewEvent.ReadingInfoLoadSuccess(response.data))
            } else {
                events.send(TimerViewEvent.ReadingInfoLoadFail)
            }
        }
    }

    fun playTimer() {
        viewModelScope.launch {
            events.send(TimerViewEvent.TogglePlayButton(playing = true))
            timer.start()
        }
    }

    fun pauseTimer() {
        viewModelScope.launch {
            events.send(TimerViewEvent.TogglePlayButton(playing = false))
            timer.pause()
        }
    }

    fun showTotalTime() {
        viewModelScope.launch {
            events.send(TimerViewEvent.ToggleTotalButton(total = true))
        }
    }

    fun hideTotalTime() {
        viewModelScope.launch {
            events.send(TimerViewEvent.ToggleTotalButton(total = false))
        }
    }

    fun setReadingInfo(readingInfo: ReadingInfo) {
        viewModelScope.launch {
            events.send(TimerViewEvent.ChangeReadingInfo(readingInfo))
        }
    }

    private fun reduce(state : TimerViewState, event : TimerViewEvent) : TimerViewState {
        return when (event) {
            TimerViewEvent.ApiResponseLoading -> {
                state.copy(buttonActive = false)
            }
            is TimerViewEvent.ReadingInfoLoadSuccess -> {
                state.copy(
                    buttonActive = true,
                    readingHistoryList = event.readingInfo.readingHistoryList
                )
            }
            TimerViewEvent.ReadingInfoLoadFail -> {
                state.copy(buttonActive = true)
            }
            TimerViewEvent.RecordFail -> {
                state.copy(buttonActive = true)
            }
            TimerViewEvent.RecordSuccess -> {
                state.copy(buttonActive = true)
            }
            is TimerViewEvent.ToggleTotalButton -> {
                addedTime = if (event.total) dailyReadingTime else 0
                setStopWatchTime(timer.getCurrentTime())
                state.copy(totalButtonToggled = event.total)
            }
            is TimerViewEvent.TogglePlayButton -> {
                state.copy(playButtonToggled = event.playing)
            }
            is TimerViewEvent.ChangeReadingInfo -> {
                state.copy(readingHistoryList = event.readingInfo.readingHistoryList)
            }
        }
    }

    @AssistedFactory
    interface AssistedViewModelFactory {
        fun create(bookId : String) : TimerViewModel
    }

    companion object {
        fun provideViewModelFactory(
            assistedFactory: AssistedViewModelFactory,
            bookId : String
        ) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(bookId) as T
            }
        }
    }


}