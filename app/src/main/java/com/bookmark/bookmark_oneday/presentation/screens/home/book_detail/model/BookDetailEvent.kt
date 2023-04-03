package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.model

import com.bookmark.bookmark_oneday.domain.model.BookDetail

sealed class BookDetailEvent {
    object GetBookDetailLoading : BookDetailEvent()
    object GetBookDetailFail : BookDetailEvent()
    class GetBookDetailSuccess(val bookDetail: BookDetail) : BookDetailEvent()
    class SetPageInfo(val currentPage : Int, val totalPage : Int) : BookDetailEvent()
}