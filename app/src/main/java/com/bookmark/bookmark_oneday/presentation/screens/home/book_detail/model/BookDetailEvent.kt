package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.model

import com.bookmark.bookmark_oneday.domain.book.model.BookDetail
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory

sealed class BookDetailEvent {
    object GetBookDetailLoading : BookDetailEvent()
    object GetBookDetailFail : BookDetailEvent()
    class GetBookDetailSuccess(val bookDetail: BookDetail) : BookDetailEvent()
    class SetPageInfo(val currentPage : Int, val totalPage : Int) : BookDetailEvent()
    class UpdateReadingHistory(val readingHistoryList : List<ReadingHistory>) : BookDetailEvent()
}