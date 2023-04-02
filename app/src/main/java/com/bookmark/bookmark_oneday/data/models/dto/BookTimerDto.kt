package com.bookmark.bookmark_oneday.data.models.dto

import com.bookmark.bookmark_oneday.domain.model.ReadingInfo

data class BookTimerDto(
    val user_id : String,
    val target_time : Int,
    val daily : Int,
    val book : BookTimerBookDto
) {
    data class BookTimerBookDto(
        val book_id : String,
        val history : List<HistoryDto>
    )

    companion object {
        fun toReadingInfo(bookTimerDto: BookTimerDto) : ReadingInfo {
            return ReadingInfo(
                dailyGoalTime = bookTimerDto.target_time,
                dailyReadingTime = bookTimerDto.daily,
                readingHistoryList = bookTimerDto.book.history.map { HistoryDto.toReadingHistory(it) }
            )
        }
    }
}
