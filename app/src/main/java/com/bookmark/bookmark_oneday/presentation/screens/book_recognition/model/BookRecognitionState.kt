package com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model

data class BookRecognitionState(
    val buttonActive : Boolean = true,
    val showLoadingDialog : Boolean = false,
    val showErrorDialog : Boolean = false
)