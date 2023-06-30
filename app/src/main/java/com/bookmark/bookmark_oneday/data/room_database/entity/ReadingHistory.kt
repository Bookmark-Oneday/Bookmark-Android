package com.bookmark.bookmark_oneday.data.room_database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "ReadingHistory",
    foreignKeys = [
        ForeignKey(
            entity = RegisteredBook::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = CASCADE
        )
    ]
)
data class ReadingHistory(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val bookId : Int,
    val userId : Int,
    val date : String,
    val timeSec : Int
)
