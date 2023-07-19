package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model

sealed class ReadingCalendarScreenEvent {
    abstract fun reduce(prevState : ReadingCalendarScreenState) : ReadingCalendarScreenState

    object CalendarCellLoading : ReadingCalendarScreenEvent() {
        override fun reduce(prevState: ReadingCalendarScreenState): ReadingCalendarScreenState {
            return prevState.copy(showCalendarCellLoading = true)
        }

    }

    object CalendarCellLoadFailure : ReadingCalendarScreenEvent() {
        override fun reduce(prevState: ReadingCalendarScreenState): ReadingCalendarScreenState {
            return prevState.copy(showCalendarCellLoading = false)
        }
    }

    class CalendarCellLoadSuccess(
        private val year: Int, private val month: Int, private val cellList: List<CalendarCell>
    ) : ReadingCalendarScreenEvent() {
        override fun reduce(prevState: ReadingCalendarScreenState): ReadingCalendarScreenState {
            return prevState.copy(
                showCalendarCellLoading = false,
                readingHistoryCalendar = ReadingHistoryCalendar(
                    year = year,
                    month = month,
                    cell = cellList
                )
            )
        }
    }

    object HistoryOfTheDayLoading : ReadingCalendarScreenEvent() {
        override fun reduce(prevState: ReadingCalendarScreenState): ReadingCalendarScreenState {
            return prevState.copy(showReadingHistoryLoading = true)
        }
    }

    object HistoryOfTheDayLoadFailure : ReadingCalendarScreenEvent() {
        override fun reduce(prevState: ReadingCalendarScreenState): ReadingCalendarScreenState {
            return prevState.copy(showReadingHistoryLoading = false, showReadingHistoryLoadingIntro = false)
        }
    }

    class HistoryOfTheDayLoadSuccess(
        private val year: Int, private val month: Int, private val day : Int, private val readingHistoryList: List<CalendarReadingHistory>
    ) : ReadingCalendarScreenEvent() {
        override fun reduce(prevState: ReadingCalendarScreenState): ReadingCalendarScreenState {
            return prevState.copy(
                showReadingHistoryLoading = false,
                showReadingHistoryLoadingIntro = false,
                readingHistoryOfTheDay = ReadingHistoryOfTheDay(
                    year = year,
                    month = month,
                    day = day,
                    readingHistory = readingHistoryList
                )
            )
        }
    }
}