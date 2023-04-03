package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation.model

import com.bookmark.bookmark_oneday.domain.model.RecognizedBook

data class BookConfirmState(
    val book: RecognizedBook?= null,
    val buttonActive : Boolean = true
)
