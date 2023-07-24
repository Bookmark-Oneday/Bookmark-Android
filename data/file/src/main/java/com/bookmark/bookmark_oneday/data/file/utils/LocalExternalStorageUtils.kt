package com.bookmark.bookmark_oneday.data.file.utils

import android.os.Environment

// https://developer.android.com/training/data-storage/app-specific?hl=ko#external-verify-availability

// Checks if a volume containing external storage is available
// for read and write.
fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}