package com.bookmark.bookmark_oneday.domain.book.model

data class ReadingInfo (
    val dailyGoalTime : Int,
    val dailyReadingTime : Int,
    val readingHistoryList : List<ReadingHistory>
)