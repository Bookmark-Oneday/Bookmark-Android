package com.bookmark.bookmark_oneday.presentation.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetReadingHistory
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseSaveReadingHistory
import com.bookmark.bookmark_oneday.presentation.model.Timer
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
    private val useCaseSaveReadingHistory: UseCaseSaveReadingHistory,
    @Assisted val bookId : String
) : ViewModel() {

    private val timer = Timer(viewModelScope, Dispatchers.Default, action = ::setStopWatchTime)

    private val events = Channel<TimerViewEvent>()
    val state : StateFlow<TimerViewState> = events.receiveAsFlow()
        .runningFold(TimerViewState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerViewState())

    private fun setStopWatchTime(time : Int) {
        viewModelScope.launch {
            events.send(TimerViewEvent.UpdateTimer(time))
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

    // 타이머 중지 시 지금까지의 독서 기록을 저장하는 api를 호출합니다.
    fun pauseTimer() {
        viewModelScope.launch {
            events.send(TimerViewEvent.TogglePlayButton(playing = false))
            timer.pause()

            events.send(TimerViewEvent.ApiResponseLoading)
            val currentTimerTime = state.value.stopWatchState.currentTime
            val response = useCaseSaveReadingHistory(bookId, currentTimerTime)

            if (response is BaseResponse.Success) {
                events.send(TimerViewEvent.RecordSuccess(response.data))
            } else {
                events.send(TimerViewEvent.RecordFail)
            }
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

    fun getReadingHistoryIfNotEmpty() : List<ReadingHistory>? {
        val readingHistoryList = state.value.readingHistoryList.map { it.copy(dateString = it.dateString.split(" ")[0]) }
        return readingHistoryList.ifEmpty { null }
    }

    private fun reduce(state : TimerViewState, event : TimerViewEvent) : TimerViewState {
        return when (event) {
            TimerViewEvent.ApiResponseLoading -> {
                state.copy(buttonActive = false)
            }
            is TimerViewEvent.ReadingInfoLoadSuccess -> {
                val stopWatchState = state.stopWatchState.copy (
                    dailyGoalTime = event.readingInfo.dailyGoalTime,
                    dailyTotalTime = event.readingInfo.dailyReadingTime
                )
                state.copy(
                    buttonActive = true,
                    readingHistoryList = event.readingInfo.readingHistoryList,
                    stopWatchState = stopWatchState
                )
            }
            TimerViewEvent.ReadingInfoLoadFail -> {
                state.copy(buttonActive = true)
            }
            TimerViewEvent.RecordFail -> {
                state.copy(buttonActive = true)
            }
            is TimerViewEvent.RecordSuccess -> {
                val stopWatchState = state.stopWatchState.copy (
                    dailyGoalTime = event.readingInfo.dailyGoalTime,
                    dailyTotalTime = event.readingInfo.dailyReadingTime,
                    currentTime = 0,
                )
                state.copy(
                    buttonActive = true,
                    stopWatchState = stopWatchState,
                    readingHistoryList = event.readingInfo.readingHistoryList
                )
            }
            is TimerViewEvent.ToggleTotalButton -> {
                val stopWatchState = state.stopWatchState.copy(showTodayTotal = event.total)
                state.copy(totalButtonToggled = event.total, stopWatchState = stopWatchState)
            }
            is TimerViewEvent.TogglePlayButton -> {
                state.copy(playButtonToggled = event.playing)
            }
            is TimerViewEvent.ChangeReadingInfo -> {
                state.copy(readingHistoryList = event.readingInfo.readingHistoryList)
            }
            is TimerViewEvent.UpdateTimer -> {
                val stopWatchState = state.stopWatchState.copy(currentTime = event.time)
                state.copy(stopWatchState = stopWatchState)
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