package com.bookmark.bookmark_oneday.domain.model

data class ReadingInfo (
    val dailyGoalTime : Int,
    val dailyReadingTime : Int,
    val readingHistoryList : List<ReadingHistory>
)