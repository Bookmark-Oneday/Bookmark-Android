package com.bookmark.bookmark_oneday.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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