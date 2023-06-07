package com.bookmark.bookmark_oneday.presentation.util

import android.content.Context
import android.content.res.Configuration

interface DarkModeChecker {
    fun isDarkMode() : Boolean
}

class DarkModeCheckerImpl constructor(private val applicationContext : Context) : DarkModeChecker {
    override fun isDarkMode(): Boolean {
        val currentNightMode = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

}