package com.bookmark.bookmark_oneday.core.model

import java.io.Serializable

// "YYYY.MM.DD HH:MM:SS"
@JvmInline
value class TimeString(val timeString : String) : Serializable {
    // "YYYY.MM.DD"
    fun getOnlyDate() : String = timeString.split(" ")[0]
    // "MM/DD"
    fun getOnlyMonthAndDay() : String = timeString.split(" ")[0].split(".").slice(1..2).joinToString("/")
}

fun String.toTimeString() : TimeString {
    val regex = Regex("^(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2}(?:\\.\\d*)?)((?:-(\\d{2}):(\\d{2})|Z)?)\$")

    val timeString = if (regex.matches(this)) {
        // 수신 데이터 형식이 "2023-04-29T11:18:25.801Z" 인 경우
        this.split(".")[0].replace('T', ' ').replace('-', '.')
    } else {
        // 수신 데이터 형식이 "2023-04-29 20:18:25" 인 경우
        this.replace('-', '.')
    }

    return TimeString(timeString)
}