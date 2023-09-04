package com.bookmark.bookmark_oneday.domain.book.util

import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistoryWithBook

fun List<ReadingHistoryWithBook>.groupByBook() : List<ReadingHistoryWithBook> {
    val readingHistory = groupBy { it.bookId }.map { entry ->
        val readingTime = entry.value.sumOf { it.time }
        val timeString = entry.value[0].dateString
        val bookTitle = entry.value[0].bookTitle
        val author = entry.value[0].author
        val bookCover = entry.value[0].bookCover

        return@map ReadingHistoryWithBook(
            dateString = timeString,
            time = readingTime,
            bookTitle = bookTitle,
            bookId = entry.key,
            author = author,
            bookCover = bookCover
        )
    }
    return readingHistory
}