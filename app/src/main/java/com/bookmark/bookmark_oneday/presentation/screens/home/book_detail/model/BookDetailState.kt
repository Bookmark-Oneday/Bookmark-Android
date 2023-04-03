package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.model

import com.bookmark.bookmark_oneday.domain.model.BookDetail

data class BookDetailState(
    val bookDetail: BookDetail? = null,
    val toolbarButtonActive : Boolean = false,
    val inputPageButtonActive : Boolean = false,
    val isShowingLoadingView : Boolean = true,
)
