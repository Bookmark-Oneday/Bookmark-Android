package com.bookmark.bookmark_oneday.presentation.util

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

val CAMERA_REQUIRED_PERMISSIONS =
    mutableListOf (
        Manifest.permission.CAMERA
    ).toTypedArray()

internal fun Context.checkCameraPermissionGranted(
    permissionList : Array<String>
) = permissionList.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

val EXACT_ALARM_PERMISSION =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Manifest.permission.SCHEDULE_EXACT_ALARM
    } else {
        null
    }

fun checkExactAlarmAvailable(context : Context) : Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

val POST_NOTIFICATIONS_33 =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        null
    }

fun checkPostNotificationAvailable(context : Context) : Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}