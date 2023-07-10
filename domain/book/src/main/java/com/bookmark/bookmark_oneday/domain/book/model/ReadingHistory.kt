package com.bookmark.bookmark_oneday.domain.book.model

import com.bookmark.bookmark_oneday.core.model.TimeString

data class ReadingHistory(val id : String, val dateString : TimeString, val time : Int)