package com.bookmark.bookmark_oneday.presentation.screens.book_recognition.model

sealed class BookRecognitionEvent {
    object SearchBookInfoLoading : BookRecognitionEvent()
    object SearchBookInfoFail : BookRecognitionEvent()
    object SearchingBarcode : BookRecognitionEvent()
}