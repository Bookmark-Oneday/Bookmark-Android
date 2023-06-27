package com.bookmark.bookmark_oneday.data.models.entity

import java.io.Serializable

data class UserEntity(
    val nickname : String,
    val profileUri : String?,
    val bio : String,
    val targetReadTime : Int
) : Serializable