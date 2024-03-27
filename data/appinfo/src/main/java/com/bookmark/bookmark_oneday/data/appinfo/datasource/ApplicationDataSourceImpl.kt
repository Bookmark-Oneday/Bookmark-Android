package com.bookmark.bookmark_oneday.data.appinfo.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class ApplicationDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ApplicationDataSource {
    override fun getFirstExecution(): Boolean {
        return sharedPreferences.getBoolean(FIRST_EXECUTION, true)
    }

    override fun setFirstExecution(isFirst: Boolean) {
        sharedPreferences.edit().putBoolean(FIRST_EXECUTION, isFirst).apply()
    }

    override fun getTimerNotificationUsed(): Boolean {
        return sharedPreferences.getBoolean(USE_TIMER_NOTIFICATION, false)
    }

    override fun setTimerNotificationUsed(useTimerNotification: Boolean) {
        sharedPreferences.edit().putBoolean(USE_TIMER_NOTIFICATION, useTimerNotification).apply()
    }

    companion object {
        private const val FIRST_EXECUTION = "first_execution"
        private const val USE_TIMER_NOTIFICATION = "use_timer_notification"
    }
}