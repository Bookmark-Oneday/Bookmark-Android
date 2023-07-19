package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model

import android.icu.util.Calendar

data class ReadingCalendarScreenState(
    val showCalendarCellLoading : Boolean = false,
    val readingHistoryCalendar: ReadingHistoryCalendar,
    val showReadingHistoryLoading : Boolean = false,
    val readingHistoryOfTheDay: ReadingHistoryOfTheDay,
    val showReadingHistoryLoadingIntro : Boolean = true
) {
    companion object {
        fun getCurrentDayInstance() : ReadingCalendarScreenState {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            return ReadingCalendarScreenState(
                readingHistoryCalendar = ReadingHistoryCalendar(year, month, listOf()),
                readingHistoryOfTheDay = ReadingHistoryOfTheDay(year, month, day, listOf())
            )
        }
    }
}

data class ReadingHistoryCalendar(
    val year : Int,
    val month : Int,
    val cell : List<CalendarCell>,
)

data class ReadingHistoryOfTheDay(
    val year : Int,
    val month : Int,
    val day : Int,
    val readingHistory : List<CalendarReadingHistory>
)