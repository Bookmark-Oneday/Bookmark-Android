package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove.model

data class BookDetailRemoveDialogState(
    val buttonActive : Boolean = true,
    val availableClose : Boolean = true,
    val showLoadingProgressBar : Boolean = false
)