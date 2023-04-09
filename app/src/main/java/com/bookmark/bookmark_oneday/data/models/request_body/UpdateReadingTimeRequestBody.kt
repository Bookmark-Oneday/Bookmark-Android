package com.bookmark.bookmark_oneday.data.models.request_body

data class UpdateReadingTimeRequestBody(
    val book_id : String,
    val reading_time : Int
)