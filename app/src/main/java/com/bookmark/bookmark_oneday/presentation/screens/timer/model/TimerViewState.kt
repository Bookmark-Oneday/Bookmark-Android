package com.bookmark.bookmark_oneday.presentation.screens.timer.model

import com.bookmark.bookmark_oneday.domain.model.ReadingHistory

data class TimerViewState (
    val totalButtonToggled : Boolean = false,
    val playButtonToggled : Boolean = false,
    val buttonActive : Boolean = true,
    val readingHistoryList : List<ReadingHistory> = listOf(),
    val stopWatchState: StopWatchState = StopWatchState()
)