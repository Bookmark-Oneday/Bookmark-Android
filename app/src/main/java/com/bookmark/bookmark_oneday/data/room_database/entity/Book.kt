package com.bookmark.bookmark_oneday.data.room_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val isbn : String,
    val title : String,
    val authors : List<String>,
    val translators : List<String>,
    val publisher : String,
    val introduce : String,
    val backgroundUri : String
)
