package com.bookmark.bookmark_oneday.presentation.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FileMapper @Inject constructor(
    @ApplicationContext private val context : Context
) {
    fun uriToImageFile(uri : Uri, fileName : String) : File {
        return uri.toBitmap(context.contentResolver).toFile(fileName, context)
    }
}

fun Uri.toBitmap(cr : ContentResolver) : Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(cr, this))
    } else {
        MediaStore.Images.Media.getBitmap(cr, this)
    }
}

fun Bitmap.toFile(fileName : String, context : Context) : File {
    val file = File(context.filesDir, fileName)
    file.createNewFile()

    val fos = FileOutputStream(file)
    this.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.close()

    return file
}