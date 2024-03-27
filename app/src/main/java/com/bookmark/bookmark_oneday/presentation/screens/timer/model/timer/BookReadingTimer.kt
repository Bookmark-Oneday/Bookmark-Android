package com.bookmark.bookmark_oneday.presentation.screens.timer.model.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookReadingTimer @Inject constructor() {
    private val _state = MutableStateFlow(TimerState())
    val state: Flow<TimerState> = _state.asStateFlow()

    private var timerJob : Job? = null
    private var startTimeInMillis = 0L

    fun start() {
        _state.update { it.copy(isPlaying = true) }
        startTimeInMillis = Calendar.getInstance().timeInMillis

        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000L)
                val currentTimeInMillis = System.currentTimeMillis()
                val timeDiff = ((currentTimeInMillis - startTimeInMillis) / 1000).toInt()
                _state.update { it.copy(second = timeDiff) }
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        _state.update { it.copy(isPlaying = false) }
    }

    fun reset() {
        _state.update { TimerState() }
    }
}