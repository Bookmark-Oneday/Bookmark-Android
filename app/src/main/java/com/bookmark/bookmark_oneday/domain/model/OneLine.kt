package com.bookmark.bookmark_oneday.domain.model

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
    val backgroundImageUrl : String ?= null,
    val createdAt : TimeString
)