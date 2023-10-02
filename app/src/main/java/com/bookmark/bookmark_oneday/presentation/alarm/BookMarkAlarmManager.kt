package com.bookmark.bookmark_oneday.presentation.alarm

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bookmark.bookmark_oneday.core.presentation.noti.NotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookMarkAlarmManager @Inject constructor(
    @ApplicationContext private val context : Context
) {
    fun clearAlarm() {
        val intent = getAlarmIntent()
        val alarmManager = getAlarmManager()
        val pendingIntent = getAlarmPendingIntent(intent)

        alarmManager.cancel(pendingIntent)
    }

    fun createAlam(
        hour : Int, minute : Int
    ) {
        clearAlarm()

        val intent = getAlarmIntent()
        val alarmManager = getAlarmManager()
        val pendingIntent = getAlarmPendingIntent(intent)
        val alarmTime = getNextAlarmTime(hour, minute)

        alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, alarmTime, pendingIntent)
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

    private fun getNextAlarmTime(hour : Int, minute : Int) : Long {
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        // 10분 전에 설정한 경우, 그날은 알람이 발생하지 않음
        val graceTimeMilli = 1000 * 60 * 10
        val currentTime = Calendar.getInstance().timeInMillis + graceTimeMilli

        return if (currentTime > alarmTime) {
            alarmTime + AlarmManager.INTERVAL_DAY
        } else {
            alarmTime
        }
    }
}