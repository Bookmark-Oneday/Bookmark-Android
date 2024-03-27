package com.bookmark.bookmark_oneday.presentation.screens.timer.model

import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory

data class TimerViewState (
    val totalButtonToggled : Boolean = false,
    val playButtonToggled : Boolean = false,
    val buttonActive : Boolean = true,
    val readingHistoryList : List<ReadingHistory> = listOf(),
    val stopWatchState: StopWatchState = StopWatchState(),
    val timerNotificationSwitch : Boolean = false
)