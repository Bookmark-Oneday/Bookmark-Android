package com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.model

sealed class TimerRemoveHistoryDialogEvent {
    object RemoveLoading : TimerRemoveHistoryDialogEvent()
    object RemoveFail : TimerRemoveHistoryDialogEvent()
    object RemoveSuccess : TimerRemoveHistoryDialogEvent()
}
