package com.bookmark.bookmark_oneday.data.book.model.request

data class UpdateReadingPageRequestBody(
    val book_id : String,
    val current_page : Int,
    val total_page : Int
)