package com.bookmark.bookmark_oneday.presentation.alarm

import android.app.AlarmManager
import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bookmark.bookmark_oneday.core.presentation.noti.NotificationManager
import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    private val notificationManager = NotificationManager()
    @Inject lateinit var applicationRepository: ApplicationRepository
    @Inject lateinit var bookMarkAlarmManager: BookMarkAlarmManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent?.action == null) return

        when (intent.action) {
            NotificationManager.notification_action -> {
                notificationManager.createReadingNotification(context)
                 createAlarm()
            }
            ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                if (checkCanUseScheduleExactAlarms(context)) {
                    createAlarm()
                } else {
                    bookMarkAlarmManager.clearAlarm()
                }
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                createAlarm()
            }
        }

    }

    private fun checkCanUseScheduleExactAlarms(context : Context) : Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms())
    }

    private fun createAlarm() = goAsync {
        val alarmInfo = applicationRepository.getAlarmInfo().first()

        if (!alarmInfo.alarmOn) return@goAsync

        bookMarkAlarmManager.clearAlarm()
        bookMarkAlarmManager.createAlam(alarmInfo.hour, alarmInfo.minute)

    }

    private fun goAsync(
        context : CoroutineContext = EmptyCoroutineContext,
        block : suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(context) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }
}