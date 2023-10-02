package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation_oneline.model

import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook

data class BookConfirmOnelineState(
    val recognizedBook: RecognizedBook ?= null,
    val showLoading : Boolean = false,
    val showError : Boolean = false
)