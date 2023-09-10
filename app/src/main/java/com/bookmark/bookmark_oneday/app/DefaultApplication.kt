package com.bookmark.bookmark_oneday.app

import android.app.Application
import com.bookmark.bookmark_oneday.core.presentation.noti.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DefaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationManager().initNotificationChannel(baseContext)
    }
}