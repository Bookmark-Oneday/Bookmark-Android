package com.bookmark.bookmark_oneday.data.models.dto

import com.bookmark.bookmark_oneday.domain.model.ReadingHistory

data class HistoryDto(
    val id : String,
    val date : String,
    val time : Int
) {
    companion object {
        fun toReadingHistory(historyDto: HistoryDto) : ReadingHistory {
            return ReadingHistory(
                id = historyDto.id,
                dateString = mapDateString(historyDto.date),
                time = historyDto.time
            )
        }

        private fun mapDateString(string : String) : String {
            return string.split("T")[0].replace('-', '.')
        }
    }
}
