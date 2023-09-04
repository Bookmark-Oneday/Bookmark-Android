package com.bookmark.bookmark_oneday.core.room.model

data class BookItemDto(
    val book_id : String,
    val title : String,
    val authors : List<String>,
    val translators : List<String>,
    val publisher : String,
    val titleImage : String,
    val reading : Boolean,
    val favorite : Boolean
)