package com.bookmark.bookmark_oneday.core.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "ReadingHistory",
    foreignKeys = [
        ForeignKey(
            entity = RegisteredBookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = CASCADE
        )
    ]
)
data class ReadingHistoryEntity(
    val bookId : Int,
    val userId : Int,
    val date : String,
    val timeSec : Int,
    @PrimaryKey(autoGenerate = true) val id : Int = 0
)
