package com.bookmark.bookmark_oneday.core.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookEntity(
    @PrimaryKey val isbn : String,
    val title : String,
    val authors : List<String>,
    val translators : List<String>,
    val publisher : String,
    val introduce : String,
    val backgroundUri : String
)
