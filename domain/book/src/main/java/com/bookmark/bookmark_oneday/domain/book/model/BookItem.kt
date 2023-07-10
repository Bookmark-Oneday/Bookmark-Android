package com.bookmark.bookmark_oneday.domain.book.model

data class BookItem(
    val id : String,
    val thumbnail : String,
    val title : String,
    val author : String,
    val reading : Boolean = false,
    val favorite : Boolean = false
)
