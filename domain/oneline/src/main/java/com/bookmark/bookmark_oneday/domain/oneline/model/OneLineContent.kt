package com.bookmark.bookmark_oneday.domain.oneline.model

data class OneLineContent(
    val id : String,
    val bookTitle : String,
    val authors : List<String>,
    val oneliner : String,
    val textColor : String,
    val textSize : String,
    val centerX : String,
    val centerY : String,
    val font : String,
    val backgroundImageUri : String?
)