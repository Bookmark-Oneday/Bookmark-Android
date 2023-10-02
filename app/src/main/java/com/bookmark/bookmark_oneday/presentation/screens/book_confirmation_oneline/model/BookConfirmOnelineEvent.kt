package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation_oneline.model

import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook

sealed class BookConfirmOnelineEvent {
    object LoadingBookSearch : BookConfirmOnelineEvent()
    object BookSearchFail : BookConfirmOnelineEvent()
    class BookSearchSuccess(val recognizedBook: RecognizedBook) : BookConfirmOnelineEvent()
}