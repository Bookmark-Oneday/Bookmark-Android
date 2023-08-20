package com.bookmark.bookmark_oneday.core.presentation.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bookmark.bookmark_oneday.core.presentation.noti.NotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class BookMarkAlarmManager @Inject constructor(
    @ApplicationContext private val context : Context
) {
    fun setAlarmOff() {
        val intent = getAlarmIntent()
        val alarmManager = getAlarmManager()
        val pendingIntent = getAlarmPendingIntent(intent)

        alarmManager.cancel(pendingIntent)
    }

    fun setAlarmOn(
        hour : Int, minute : Int
    ) {
        setAlarmOff()

        val intent = getAlarmIntent()
        val alarmManager = getAlarmManager()
        val pendingIntent = getAlarmPendingIntent(intent)
        val calendar = getCalendar(hour, minute)

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun getAlarmIntent() : Intent {
        return Intent(context, AlarmReceiver::class.java).apply {
            action = NotificationManager.notification_action
        }
    }

    private fun getAlarmManager() = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun getAlarmPendingIntent(intent : Intent) : PendingIntent {
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        return PendingIntent.getBroadcast(
            context, NotificationManager.BookNotificationId, intent, flags
        )
    }

    private fun getCalendar(hour : Int, minute : Int) : Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
    }
}