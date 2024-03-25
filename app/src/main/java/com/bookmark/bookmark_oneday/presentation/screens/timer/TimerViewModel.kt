package com.bookmark.bookmark_oneday.presentation.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseGetReadingHistory
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseSaveReadingHistory
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.TimerViewEvent
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.TimerViewState
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.timer.BookReadingTimer
import com.bookmark.bookmark_oneday.presentation.util.MutableEventFlow
import com.bookmark.bookmark_oneday.presentation.util.asEventFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimerViewModel @AssistedInject constructor(
    private val useCaseGetReadingHistory: UseCaseGetReadingHistory,
    private val useCaseSaveReadingHistory: UseCaseSaveReadingHistory,
    private val timer : BookReadingTimer,
    @Assisted val bookId : String
) : ViewModel() {

    private val events = Channel<TimerViewEvent>()
    val state : StateFlow<TimerViewState> = events.receiveAsFlow()
        .runningFold(TimerViewState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerViewState())

    // 기록 추가 및 삭제와 같은 이벤트로 인해, 기록의 변화가 발생했는지 여부를 나타냅니다.
    private var readingHistoryChanged = false

    private val _timerServiceActionEvent = MutableEventFlow<String>()
    val timerServiceActionEvent = _timerServiceActionEvent.asEventFlow()

    init {
        viewModelScope.launch {
            timer.state.collectLatest {
                events.send(TimerViewEvent.UpdateTimer(it.second))
            }
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
                timer.reset()
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
            events.send(TimerViewEvent.UpdateReadingInfo(readingInfo))
        }
    }

    fun getReadingHistoryIfChanged() : List<ReadingHistory>? {
        val readingHistoryList = state.value.readingHistoryList
        return if (readingHistoryChanged) readingHistoryList else null
    }

    fun setTimerNotificationUseSwitchState(isChecked : Boolean) {
        viewModelScope.launch {
            events.send(TimerViewEvent.ToggleTimerNotificationSwitch(isChecked))
        }
    }

    private fun emitServiceActionEvent(action : String) {
        viewModelScope.launch {
            _timerServiceActionEvent.emit(action)
        }
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
                readingHistoryChanged = true
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
            is TimerViewEvent.UpdateReadingInfo -> {
                readingHistoryChanged = true
                val stopWatchState = state.stopWatchState.copy (
                    dailyGoalTime = event.readingInfo.dailyGoalTime,
                    dailyTotalTime = event.readingInfo.dailyReadingTime
                )
                state.copy(
                    readingHistoryList = event.readingInfo.readingHistoryList,
                    stopWatchState = stopWatchState
                )
            }
            is TimerViewEvent.UpdateTimer -> {
                val stopWatchState = state.stopWatchState.copy(currentTime = event.time)
                state.copy(stopWatchState = stopWatchState)
            }
            is TimerViewEvent.ToggleTimerNotificationSwitch -> {
                val callServiceStartAction = !state.timerNotificationSwitch && event.useTimerNotification
                val callServiceStopAction = state.timerNotificationSwitch && !event.useTimerNotification

                if (callServiceStartAction) {
                    emitServiceActionEvent(TimerService.START)
                }
                if (callServiceStopAction) {
                    emitServiceActionEvent(TimerService.PAUSE)
                }

                state.copy(timerNotificationSwitch = event.useTimerNotification)
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