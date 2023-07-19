package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistoryWithBook
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseGetReadingHistory
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseGetUser
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.CalendarCell
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.CalendarReadingHistory
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.ReadingCalendarScreenEvent
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.ReadingCalendarScreenState
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.util.firstDaysOfNextMonth
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.util.getDayAmountOfMonth
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.util.lastDaysOfPrevMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingCalendarViewModel @Inject constructor(
    private val useCaseGetReadingHistory: UseCaseGetReadingHistory,
    useCaseGetUserInfo : UseCaseGetUser
) : ViewModel() {

    private val goalTime = useCaseGetUserInfo.getGoalReadingTime()

    private val events = Channel<ReadingCalendarScreenEvent>()
    val state : StateFlow<ReadingCalendarScreenState> = events.receiveAsFlow()
        .runningFold(ReadingCalendarScreenState.getCurrentDayInstance()) { state, event ->
            event.reduce(state)
        }
        .combine(goalTime) { state, goalTime ->
            return@combine state.copy(
                readingHistoryCalendar = state.readingHistoryCalendar.copy(
                    cell = state.readingHistoryCalendar.cell.map { cell ->
                        cell.copy(goalTimeMinute = goalTime)
                    }
                )
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ReadingCalendarScreenState.getCurrentDayInstance())


    init {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        loadCalendar(year, month)
    }

    fun loadReadingHistoryOfTheDay(year : Int, month : Int, day : Int) {
        if (state.value.showCalendarCellLoading || state.value.showReadingHistoryLoading) return

        viewModelScope.launch {
            events.send(ReadingCalendarScreenEvent.HistoryOfTheDayLoading)

            val response = useCaseGetReadingHistory.getHistoryOfDay(year, month, day)
            if (response is BaseResponse.Success<List<ReadingHistoryWithBook>>) {
                val bookList = response.data.map { readingHistoryWithBook ->
                    CalendarReadingHistory(
                        id = readingHistoryWithBook.bookId,
                        cover = readingHistoryWithBook.bookCover,
                        title = readingHistoryWithBook.bookTitle,
                        author = readingHistoryWithBook.author,
                        time = readingHistoryWithBook.time
                    )
                }
                events.send(ReadingCalendarScreenEvent.HistoryOfTheDayLoadSuccess(year, month, day, bookList))
            } else {
                events.send(ReadingCalendarScreenEvent.HistoryOfTheDayLoadFailure)
            }

        }
    }

    fun loadCalendar(year : Int, month : Int) {
        viewModelScope.launch {
            events.send(ReadingCalendarScreenEvent.CalendarCellLoading)

            val goalTime = goalTime.first()
            val response = useCaseGetReadingHistory.getHistoryOfMonth(year, month)
            if (response is BaseResponse.Success<List<ReadingHistory>>) {
                val cellList = mapToCalendarCell(year, month, response.data, goalTime)
                events.send(ReadingCalendarScreenEvent.CalendarCellLoadSuccess(year, month, cellList))
            } else {
                events.send(ReadingCalendarScreenEvent.CalendarCellLoadFailure)
            }

        }
    }

    private fun mapToCalendarCell(year : Int, month : Int, readingHistoryList: List<ReadingHistory>, goalTime : Int) : List<CalendarCell> {
        val nextCell = firstDaysOfNextMonth(year, month).map { day ->
            CalendarCell(
                year = if (month == 12) year + 1 else year,
                month = if (month == 12) 1 else month + 1,
                day = day,
                readTimeMinute = 0,
                goalTimeMinute = goalTime
            )
        }

        val prevCell = lastDaysOfPrevMonth(year, month).map { day ->
            CalendarCell(
                year = if (month == 1) year - 1 else year,
                month = if (month == 1) 12 else month - 1,
                day = day,
                readTimeMinute = 0,
                goalTimeMinute = goalTime
            )
        }

        val currentCell = Array(getDayAmountOfMonth(year, month)) {
            CalendarCell(
                year = year,
                month = month,
                day = it + 1,
                readTimeMinute = 0,
                goalTimeMinute = goalTime
            )
        }
        for (readingHistory in readingHistoryList) {
            val dayIndex = readingHistory.dateString.getDate() - 1
            currentCell[dayIndex] = currentCell[dayIndex].copy(readTimeMinute = readingHistory.time / 60)
        }

        return prevCell + currentCell.toList() + nextCell
    }

    fun getYear() = state.value.readingHistoryCalendar.year

    fun getMonth() = state.value.readingHistoryCalendar.month
}