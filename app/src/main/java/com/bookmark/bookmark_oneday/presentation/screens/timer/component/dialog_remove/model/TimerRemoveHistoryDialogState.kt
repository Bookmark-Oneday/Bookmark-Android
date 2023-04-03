package com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.model

data class TimerRemoveHistoryDialogState(
    val buttonActive : Boolean = true,
    val availableClose : Boolean = true,
    val showLoadingProgressBar : Boolean = false
)
