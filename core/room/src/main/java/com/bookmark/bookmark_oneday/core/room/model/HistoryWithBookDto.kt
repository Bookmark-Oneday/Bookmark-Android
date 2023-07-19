package com.bookmark.bookmark_oneday.core.room.model

data class HistoryWithBookDto (
    val bookId : Int,
    val date : String,
    val time : Int,
    val title : String,
    val authors : List<String>,
    val titleImage : String,
)
