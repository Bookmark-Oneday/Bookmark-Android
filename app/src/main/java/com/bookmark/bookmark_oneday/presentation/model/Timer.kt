package com.bookmark.bookmark_oneday.presentation.model

import kotlinx.coroutines.*

class Timer(
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val action : (Int) -> Unit = {}
) {
    private var currentTime = 0
    private var timerJob : Job ?= null

    fun start() {
        timerJob = coroutineScope.launch(dispatcher) {
            while(true) {
                delay(1000L)
                action(++currentTime)
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
    }

    fun getCurrentTime() = currentTime
}