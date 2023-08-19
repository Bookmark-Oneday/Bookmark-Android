package com.bookmark.bookmark_oneday.core.presentation.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bookmark.bookmark_oneday.core.presentation.noti.NotificationManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    private val notificationManager = NotificationManager()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action == NotificationManager.notification_action) {
            notificationManager.createReadingNotification(context)
        }
    }
}