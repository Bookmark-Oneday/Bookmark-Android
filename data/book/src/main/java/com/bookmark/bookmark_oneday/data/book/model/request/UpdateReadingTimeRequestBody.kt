package com.bookmark.bookmark_oneday.data.book.model.request

data class UpdateReadingTimeRequestBody(
    val book_id : String,
    val reading_time : Int
)