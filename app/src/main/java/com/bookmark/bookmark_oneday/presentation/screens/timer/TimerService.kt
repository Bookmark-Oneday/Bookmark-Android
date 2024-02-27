package com.bookmark.bookmark_oneday.presentation.screens.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bookmark.bookmark_oneday.R
import com.bookmark.bookmark_oneday.presentation.screens.timer.model.timer.BookReadingTimer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {
    @Inject
    lateinit var timer : BookReadingTimer
    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        initNotificationManager()
        initObserver()

        intent?.getStringExtra(TIMER_ACTION)?.let { action -> handleActions(action) }

        return START_STICKY
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "timer",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun initNotificationManager() {
        notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
    }

    private fun initObserver() {
        CoroutineScope(Dispatchers.Default).launch {
            timer.state.collectLatest {
                if (it.isPlaying) {
                    notificationManager.notify(1, buildTimerNotification(it.second))
                } else {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }
        }
    }

    private fun handleActions(action : String) {
        when (action) {
            START -> {
                startForeground(1, buildTimerNotification(0))
                timer.start()
            }
            PAUSE -> {
                timer.pause()
            }
            RESET -> {
                timer.reset()
            }
        }
    }

    private fun buildTimerNotification(totalSecond : Int) : Notification {
        val hours = totalSecond / 3600
        val minutes = totalSecond / 60
        val seconds = totalSecond % 60

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("timer")
            .setOngoing(true)
            .setContentText("${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}")
            .setSmallIcon(R.drawable.ic_logo)
            .setAutoCancel(false)
            .build()
    }

    companion object {
        // channel id
        const val CHANNEL_ID = "bookmark_oneday_timer"

        // Service Actions
        const val START = "START"
        const val PAUSE = "PAUSE"
        const val RESET = "RESET"

        // Intent Extras
        const val TIMER_ACTION = "TIMER_ACTION"
    }
}