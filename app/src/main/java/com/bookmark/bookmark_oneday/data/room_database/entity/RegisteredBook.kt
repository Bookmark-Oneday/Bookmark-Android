package com.bookmark.bookmark_oneday.data.room_database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "registeredBook",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["isbn"],
            childColumns = ["isbn"],
            onDelete = CASCADE
        )
    ]
)
data class RegisteredBook(
    val isbn : String,
    val totalPage : Int,
    val currentPage : Int,
    val favorite : Boolean = false,
    val reading : Boolean = false,
    val registeredAt : String,
    @PrimaryKey(autoGenerate = true) val id : Int = 0
)