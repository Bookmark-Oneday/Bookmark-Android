package com.bookmark.bookmark_oneday.domain.book.model

import com.bookmark.bookmark_oneday.core.model.TimeString

data class ReadingHistoryWithBook(
    val bookId : String, val dateString : TimeString, val time : Int, val bookTitle : String, val author : String, val bookCover : String
)