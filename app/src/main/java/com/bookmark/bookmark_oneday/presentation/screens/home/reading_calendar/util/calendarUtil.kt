package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.util

import android.content.Context
import com.bookmark.bookmark_oneday.R
import java.util.Calendar

fun Int.monthString(context : Context) : String {
    return when (this) {
        1 -> {
            context.getString(R.string.label_jan)
        }
        2 -> {
            context.getString(R.string.label_feb)
        }
        3 -> {
            context.getString(R.string.label_mar)
        }
        4 -> {
            context.getString(R.string.label_apr)
        }
        5 -> {
            context.getString(R.string.label_may)
        }
        6 -> {
            context.getString(R.string.label_jun)
        }
        7 -> {
            context.getString(R.string.label_jul)
        }
        8 -> {
            context.getString(R.string.label_aug)
        }
        9 -> {
            context.getString(R.string.label_sep)
        }
        10 -> {
            context.getString(R.string.label_oct)
        }
        11 -> {
            context.getString(R.string.label_nov)
        }
        12 -> {
            context.getString(R.string.label_dec)
        }
        else -> {
            throw IllegalArgumentException("to get monthString, integer must be in 1..12 : current value : $this")
        }
    }
}

/**
 * 각 월마다 일의 총 개수를 리턴합니다.
 */
fun getDayAmountOfMonth(year : Int, month : Int) : Int {
    return when (month) {
        2 -> {
            if (year % 4 != 0 || (year % 100 == 0 && year % 400 != 0)) { 28 }
            else { 29 }
        }
        in listOf(1,3,5,7,8,10,12) -> { 31 }
        else -> { 30 }
    }
}

/**
 * 달력 상에서 이전달에 해당하는 요일의 리스트를 리턴합니다.
 */
fun lastDaysOfPrevMonth(year: Int, month: Int): List<Int> {
    val calendar = Calendar.getInstance()
    val prevMonthIndex = month - 1

    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, prevMonthIndex)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val amountOfDayOfPrevMonth = getDayAmountOfMonth(year, month - 1)

    return List(startDayOfWeek - 1) { i -> amountOfDayOfPrevMonth - (startDayOfWeek - 2 - i) }
}

/**
 * 달력 상에서 다음달에 해당하는 요일의 리스트를 리턴합니다.
 */
fun firstDaysOfNextMonth(year : Int, month : Int) : List<Int> {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    return List(8 - startDayOfWeek) { it + 1 }
}