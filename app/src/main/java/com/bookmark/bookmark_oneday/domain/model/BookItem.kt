package com.bookmark.bookmark_oneday.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookItem(
    val id : String,
    val thumbnail : String,
    val title : String,
    val author : String,
    val reading : Boolean = false,
    val favorite : Boolean = false
) : Parcelable
