package com.bookmark.bookmark_oneday.presentation.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.presentation.model.Timer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    private val timer = Timer(viewModelScope, Dispatchers.Default, goalTime = 680, action = ::setStopWatchTime)

    private val events = Channel<TimerViewEvent>()
    val state : StateFlow<TimerViewState> = events.receiveAsFlow()
        .runningFold(TimerViewState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerViewState())

    private val _sideEffects = MutableSharedFlow<TimerViewSideEffect>()
    val sideEffects = _sideEffects.asSharedFlow()

    private val _stopWatchState = MutableStateFlow(StopWatchState())
    val stopWatchState = _stopWatchState.asStateFlow()

    fun setReadingHistory(historyList : List<ReadingHistory>) {
        viewModelScope.launch {
            events.send(TimerViewEvent.ChangeHistoryList(historyList))
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

    fun setStopWatchTime(time : Int) {
        viewModelScope.launch {
            _stopWatchState.value = StopWatchState(
                timeString = String.format("%02d:%02d", time / 60, time % 60),
                progress = time
            )
        }
    }

    private fun reduce(state : TimerViewState, event : TimerViewEvent) : TimerViewState {
        return when (event) {
            TimerViewEvent.ApiResponseLoading -> {
                state.copy(buttonActive = false)
            }
            TimerViewEvent.RecordFail -> {
                state.copy(buttonActive = true)
            }
            TimerViewEvent.RecordSuccess -> {
                state.copy(buttonActive = true)
            }
            is TimerViewEvent.ToggleTotalButton -> {
                state.copy(totalButtonToggled = event.total)
            }
            is TimerViewEvent.TogglePlayButton -> {
                state.copy(playButtonToggled = event.playing)
            }
            TimerViewEvent.RemoveHistoryFail -> {
                state.copy(buttonActive = true)
            }
            TimerViewEvent.RemoveHistoryAllSuccess -> {
                state.copy(
                    buttonActive = true,
                    readingHistoryList = listOf()
                )
            }
            is TimerViewEvent.ChangeHistoryList -> {
                state.copy(readingHistoryList = event.readingHistoryList)
            }
            is TimerViewEvent.RemoveHistoryItemSuccess -> {
                val filteredReadingHistory = state.readingHistoryList.filter { it.id != event.targetItemId }
                state.copy(
                    buttonActive = true,
                    readingHistoryList = filteredReadingHistory
                )
            }
        }
    }

}

data class StopWatchState (
    val timeString : String = "00:00",
    val progress : Int = 0
)

sealed class TimerViewSideEffect {
    object ClearReadingHistory : TimerViewSideEffect()
    class RemoveReadingHistory(val position : Int) : TimerViewSideEffect()
}

data class TimerViewState (
    val totalButtonToggled : Boolean = false,
    val playButtonToggled : Boolean = false,
    val buttonActive : Boolean = true,
    val readingHistoryList : List<ReadingHistory> = listOf()
)

sealed class TimerViewEvent {
    object ApiResponseLoading : TimerViewEvent()
    object RecordFail : TimerViewEvent()
    object RecordSuccess : TimerViewEvent()
    class ToggleTotalButton(val total : Boolean) : TimerViewEvent()
    class TogglePlayButton(val playing : Boolean) : TimerViewEvent()
    object RemoveHistoryFail : TimerViewEvent()
    object RemoveHistoryAllSuccess : TimerViewEvent()
    class RemoveHistoryItemSuccess(val targetItemId : Int) : TimerViewEvent()
    class ChangeHistoryList(val readingHistoryList : List<ReadingHistory>) : TimerViewEvent()
}
