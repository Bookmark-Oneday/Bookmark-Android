package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation.model

import com.bookmark.bookmark_oneday.domain.model.RecognizedBook

sealed class BookConfirmEvent {
    object BookConfirmEventNormal : BookConfirmEvent()
    object RegisterBookLoading : BookConfirmEvent()
    object RegisterBookDuplicate : BookConfirmEvent()
    object RegisterBookFail : BookConfirmEvent()
    class ChangeBookInfo(val book: RecognizedBook) : BookConfirmEvent()
}