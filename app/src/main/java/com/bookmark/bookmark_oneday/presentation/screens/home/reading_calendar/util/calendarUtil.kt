package com.bookmark.bookmark_oneday.presentation.screens.home.reading_calendar.util

import android.content.Context
import com.bookmark.bookmark_oneday.R

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