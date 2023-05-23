package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model

sealed class EditTextDetailState {
    object Normal : EditTextDetailState()
    object IME : EditTextDetailState()
    object Font : EditTextDetailState()
}