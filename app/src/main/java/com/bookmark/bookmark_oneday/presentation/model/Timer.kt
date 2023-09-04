package com.bookmark.bookmark_oneday.presentation.model

import kotlinx.coroutines.*

class Timer(
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val action : (Int) -> Unit = {}
) {
    private var timerJob : Job ?= null
    private var startTimeMilli = 0L

    fun start() {
        startTimeMilli = System.currentTimeMillis()
        timerJob = coroutineScope.launch(dispatcher) {
            while(true) {
                delay(1000L)
                val currentTime = System.currentTimeMillis()
                val timeDiff = ((currentTime - startTimeMilli) / 1000).toInt()
                action(timeDiff)
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
    }

    fun resetTime() {
        startTimeMilli = 0L
    }
}