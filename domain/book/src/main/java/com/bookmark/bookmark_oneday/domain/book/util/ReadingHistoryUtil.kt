package com.bookmark.bookmark_oneday.domain.book.util

import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory

fun List<ReadingHistory>.groupByDate() : List<ReadingHistory> {
    val readingHistory = groupBy { it.dateString.getOnlyMonthAndDay() }.map { entry ->
        val readingTime = entry.value.sumOf { it.time }
        val timeString = entry.value[0].dateString
        return@map ReadingHistory(id = "sumOf_${entry.key}", dateString = timeString, readingTime)
    }
    return readingHistory
}