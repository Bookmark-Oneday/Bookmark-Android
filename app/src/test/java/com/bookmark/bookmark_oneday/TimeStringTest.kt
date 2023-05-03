package com.bookmark.bookmark_oneday

import com.bookmark.bookmark_oneday.domain.model.toTimeString
import org.junit.Test
import org.junit.Assert.*

class TimeStringTest {

    @Test
    fun stringTimeRegex() {
        val regex = Regex("^(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2}(?:\\.\\d*)?)((?:-(\\d{2}):(\\d{2})|Z)?)\$")
        val testCase1 = "2023-04-29T11:18:25.801Z"
        val testCase2 = "2023-04-29 20:18:25"

        assert(regex.matches(testCase1))
        assertFalse(regex.matches(testCase2))
    }

    @Test
    fun mapStringToTimeString() {
        val testCase1 = "2023-04-29T11:18:25.801Z"
        val testCase2 = "2023-04-29 20:18:25"

        assertEquals("2023.04.29 11:18:25", testCase1.toTimeString().timeString)
        assertEquals("2023.04.29 20:18:25", testCase2.toTimeString().timeString)
    }

    @Test
    fun timeStringToFunctions() {
        val timeString = "2023-04-29T11:18:25.801Z".toTimeString()

        assertEquals("2023.04.29", timeString.getOnlyDate())
        assertEquals("04/29", timeString.getOnlyMonthAndDay())
    }
}