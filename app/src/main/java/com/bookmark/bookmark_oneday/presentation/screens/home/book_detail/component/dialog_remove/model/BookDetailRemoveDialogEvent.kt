package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove.model

sealed class BookDetailRemoveDialogEvent {
    object RemoveLoading : BookDetailRemoveDialogEvent()
    object RemoveFail : BookDetailRemoveDialogEvent()
    object RemoveSuccess : BookDetailRemoveDialogEvent()
}