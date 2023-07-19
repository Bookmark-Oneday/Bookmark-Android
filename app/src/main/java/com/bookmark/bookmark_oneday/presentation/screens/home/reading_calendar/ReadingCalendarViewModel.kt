package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseGetReadingHistory
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.CalendarCell
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.CalendarReadingHistory
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.ReadingCalendarScreenEvent
import com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model.ReadingCalendarScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingCalendarViewModel @Inject constructor(
    private val useCaseGetReadingHistory: UseCaseGetReadingHistory
) : ViewModel() {

    private val events = Channel<ReadingCalendarScreenEvent>()
    val state : StateFlow<ReadingCalendarScreenState> = events.receiveAsFlow()
        .runningFold(ReadingCalendarScreenState.getCurrentDayInstance()) { state, event ->
            event.reduce(state)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ReadingCalendarScreenState.getCurrentDayInstance())

    init {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        loadCalendar(year, month)
    }

    fun loadReadingHistoryOfTheDay(year : Int, month : Int, day : Int) {
        viewModelScope.launch {
            events.send(ReadingCalendarScreenEvent.HistoryOfTheDayLoading)

            // 대충 데이터 로드 성공했다 가정
            val tempReadingHistory = listOf(
                CalendarReadingHistory("1", "", "title", "author", 10),
                CalendarReadingHistory("1", "", "title2", "author2", 10),
                CalendarReadingHistory("1", "", "title3", "author3", 10)
            )

            delay(3000L)

            events.send(ReadingCalendarScreenEvent.HistoryOfTheDayLoadSuccess(year, month, day, tempReadingHistory))
        }
    }

    fun loadCalendar(year : Int, month : Int) {
        viewModelScope.launch {
            events.send(ReadingCalendarScreenEvent.CalendarCellLoading)

            delay(3000L)

            // 대충 데이터 로드 성공했다 가정
            val tempCell = listOf<CalendarCell>(
                CalendarCell(year, month, 1, 0.2f),
                CalendarCell(year, month, 1, 1.2f),
                CalendarCell(year, month, 1, 0.7f),
                CalendarCell(year, month, 1, 0.9f),
                CalendarCell(year, month, 1, 0.1f),
                CalendarCell(year, month, 1, 0.0f),
                CalendarCell(year, month, 1, 1.0f),
                CalendarCell(year, month, 1, 0.2f),
                CalendarCell(year, month, 1, 1.2f),
                CalendarCell(year, month, 1, 0.7f),
                CalendarCell(year, month, 1, 0.9f),
                CalendarCell(year, month, 1, 0.1f),
                CalendarCell(year, month, 1, 0.0f),
                CalendarCell(year, month, 1, 1.0f)
            )

            events.send(ReadingCalendarScreenEvent.CalendarCellLoadSuccess(year, month, tempCell))
        }
    }

    fun getYear() = state.value.readingHistoryCalendar.year

    fun getMonth() = state.value.readingHistoryCalendar.month
}