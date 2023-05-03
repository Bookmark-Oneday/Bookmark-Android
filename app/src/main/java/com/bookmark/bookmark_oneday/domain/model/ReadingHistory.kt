package com.bookmark.bookmark_oneday.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadingHistory(val id : String, val dateString : TimeString, val time : Int) : Parcelable