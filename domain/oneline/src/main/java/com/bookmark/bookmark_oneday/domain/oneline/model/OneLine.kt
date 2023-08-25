package com.bookmark.bookmark_oneday.domain.oneline.model

import com.bookmark.bookmark_oneday.core.model.TimeString

data class OneLine(
    val id : String,
    val userProfile: UserProfile,
    val bookId : String,
    val bookInfo : String,
    val oneliner : String,
    val textColor : String,
    val centerYPosition : Float,
    val centerXPosition : Float,
    val fontSize : Int,
    val font : String = "default",
    val backgroundImageUrl : String ?= null,
    val createdAt : TimeString
)