package com.bookmark.bookmark_oneday.domain.model

data class UserProfile (
    val id : String,
    val profileImageUrl : String ?= null,
    val nickname : String
)