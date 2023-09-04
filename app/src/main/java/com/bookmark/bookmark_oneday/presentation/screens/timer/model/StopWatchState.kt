package com.bookmark.bookmark_oneday.presentation.screens.timer.model

import kotlin.math.max

data class StopWatchState (
    val showTodayTotal : Boolean = false,
    val dailyTotalTime : Int = 0,
    val dailyGoalTime : Int = 0,
    val currentTime : Int = 0
) {
    fun getTimerText() : String {
        val timerTime = if (showTodayTotal) {
            currentTime + dailyTotalTime
        } else {
            currentTime
        }

        return String.format("%02d:%02d", timerTime / 60, timerTime % 60)
    }

    fun getProgress() : Int {
        val timerTime = if (showTodayTotal) {
            currentTime + dailyTotalTime
        } else {
            currentTime
        }

        return timerTime * 100 / max(1, dailyGoalTime)
    }
}