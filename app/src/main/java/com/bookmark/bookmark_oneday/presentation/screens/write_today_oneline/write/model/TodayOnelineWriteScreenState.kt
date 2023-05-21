package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model

sealed class TodayOnelineWriteScreenState {
    object PreView : TodayOnelineWriteScreenState()
    object TextEdit : TodayOnelineWriteScreenState()
    object Uploading : TodayOnelineWriteScreenState()
}
