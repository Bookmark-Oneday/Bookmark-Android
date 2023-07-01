package com.bookmark.bookmark_oneday.data.room_database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["isbn"],
            childColumns = ["isbn"],
            onDelete = CASCADE
        )
    ]
)
data class OneLine(
    val userId : Int,
    val isbn : String,
    val color : String,
    val centerX : String,
    val centerY : String,
    val content : String,
    val font : String,
    val fontSize : String,
    val backgroundUri : String ?= null,
    val createdAt : String,
    @PrimaryKey(autoGenerate = true) val id : Int = 0
)
