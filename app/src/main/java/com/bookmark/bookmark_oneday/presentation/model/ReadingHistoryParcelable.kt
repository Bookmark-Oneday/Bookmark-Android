package com.bookmark.bookmark_oneday.presentation.model

import android.os.Parcelable
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory
import com.bookmark.bookmark_oneday.core.model.toTimeString
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadingHistoryParcelable(val id : String, val dateString : String, val time : Int) : Parcelable {
    companion object {
        fun fromReadingHistory(readingHistory: ReadingHistory) : ReadingHistoryParcelable {
            return ReadingHistoryParcelable(
                id = readingHistory.id,
                dateString = readingHistory.dateString.timeString,
                time = readingHistory.time
            )
        }
    }

    fun toReadingHistory() : ReadingHistory {
        return ReadingHistory(id = id, dateString = dateString.toTimeString(), time = time)
    }
}