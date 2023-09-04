package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_editpage.model

sealed class BookDetailEditPageEvent {
    object EditPageLoading : BookDetailEditPageEvent()
    object EditPageFail : BookDetailEditPageEvent()
    object EditPageSuccess : BookDetailEditPageEvent()
}