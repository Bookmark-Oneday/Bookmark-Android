package com.bookmark.bookmark_oneday.presentation.util

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.bookmark.bookmark_oneday.R

fun getReadingTimeString(context : Context, timeMinute : Int) : String {
    val hour = timeMinute / 60
    val minute = timeMinute % 60

    val text = if (hour >= 1 && minute == 0) {
        context.getString(R.string.label_signup_set_reading_time_format_exclude_minute, hour)
    } else if (hour == 0) {
        context.getString(R.string.label_signup_set_reading_time_format_exclude_hour, minute)
    } else {
        context.getString(R.string.label_signup_set_reading_time_format, hour, minute)
    }

    return text
}

fun getTimeCaptionHtmlText(context : Context, timeMinute : Int) : Spanned {
    val averageReadingTime2022 = 30
    val timeDiff = timeMinute - averageReadingTime2022

    val text = if (timeDiff >= 0) {
        HtmlCompat.fromHtml(context.getString(R.string.html_caption_signup_set_reading_time_more, timeDiff), HtmlCompat.FROM_HTML_MODE_COMPACT)
    } else {
        HtmlCompat.fromHtml(context.getString(R.string.html_caption_signup_set_reading_time_less, -timeDiff), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    return text
}