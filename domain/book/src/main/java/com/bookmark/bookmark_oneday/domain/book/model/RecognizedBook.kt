package com.bookmark.bookmark_oneday.domain.book.model

data class RecognizedBook(
    val title : String,
    val content : String,
    val authors : List<String>,
    val translators : List<String>,
    val thumbnail_url : String,
    val isbn : String,
    val total_page : Int,
    val meta : String,
    val publisher : String,
    val dateTime : String
) : java.io.Serializable
