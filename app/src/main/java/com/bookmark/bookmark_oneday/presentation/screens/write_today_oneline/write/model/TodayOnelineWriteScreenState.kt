package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model

sealed class TodayOnelineWriteScreenState {
    object TextMove : TodayOnelineWriteScreenState()
    class TextEdit(val editTextDetailState: EditTextDetailState) : TodayOnelineWriteScreenState()
    object Uploading : TodayOnelineWriteScreenState()
}
