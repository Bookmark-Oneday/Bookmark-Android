package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.model

data class CalendarCell(val year : Int, val month : Int, val day : Int, val readTimeMinute : Int, val goalTimeMinute : Int) {
    val readingTimeOfTargetTime : Float get() { return readTimeMinute / goalTimeMinute.toFloat() }
}