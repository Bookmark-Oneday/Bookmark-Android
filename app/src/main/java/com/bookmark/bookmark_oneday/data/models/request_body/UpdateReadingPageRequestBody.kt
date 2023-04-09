package com.bookmark.bookmark_oneday.data.models.request_body

data class UpdateReadingPageRequestBody(
    val book_id : String,
    val current_page : Int,
    val total_page : Int
)