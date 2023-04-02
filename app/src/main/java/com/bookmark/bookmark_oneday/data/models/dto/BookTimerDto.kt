package com.bookmark.bookmark_oneday.data.models.dto

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
}
