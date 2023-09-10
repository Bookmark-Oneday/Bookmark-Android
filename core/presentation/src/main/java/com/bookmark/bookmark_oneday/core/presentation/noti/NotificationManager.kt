package com.bookmark.bookmark_oneday.core.presentation.noti

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bookmark.bookmark_oneday.core.presentation.R

class NotificationManager {

    fun initNotificationChannel(context : Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    channelId,
                    BookNotificationName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "bookMark-oneDay notification"
                }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    fun createReadingNotification(
        context : Context
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_logo)
            setContentTitle(context.getString(R.string.alarm_title))
            setContentText(context.getString(R.string.alarm_text))
            setAutoCancel(true)
        }.build()

        notificationManager.notify(BookNotificationId, builder)
    }

    companion object {
        const val channelId = "135"
        const val BookNotificationId = 135
        const val BookNotificationName = "book_notification"

        const val notification_action = "bookmark-oneday-reading-time-notification"
    }
}