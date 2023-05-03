package com.bookmark.bookmark_oneday.data.models.dto

import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.model.toTimeString

data class HistoryDto(
    val id : String,
    val date : String,
    val time : Int
) {
    companion object {
        fun toReadingHistory(historyDto: HistoryDto) : ReadingHistory {
            return ReadingHistory(
                id = historyDto.id,
                dateString = historyDto.date.toTimeString(),
                time = historyDto.time
            )
        }

    }

}
