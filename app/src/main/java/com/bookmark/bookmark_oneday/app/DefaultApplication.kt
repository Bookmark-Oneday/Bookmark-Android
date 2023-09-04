package com.bookmark.bookmark_oneday.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DefaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}