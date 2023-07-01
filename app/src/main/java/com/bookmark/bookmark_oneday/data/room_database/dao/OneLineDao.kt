package com.bookmark.bookmark_oneday.data.room_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bookmark.bookmark_oneday.data.room_database.entity.OneLine

@Dao
interface OneLineDao {
    @Insert
    suspend fun insertOneLine(oneLine: OneLine)

    data class OneLineAndBookInfo(
        val id : String,
        val isbn : String,
        val title : String,
        val authors : List<String>,
        val oneliner : String,
        val color : String,
        val topPosition : String,
        val leftPosition : String,
        val font : String,
        val font_size : String,
        val bg_image_url : String?,
        val created_at : String
    )
    @Query(
        "SELECT OneLine.id AS id, OneLine.isbn AS isbn, Book.title AS title, Book.authors AS authors, content AS oneliner, " +
        "OneLine.color AS color, OneLine.centerX AS leftPosition, OneLine.centerY AS topPosition, " +
        "OneLine.font AS font, OneLine.fontSize AS font_size, OneLine.backgroundUri AS bg_image_url, OneLine.createdAt as created_at " +
        "FROM OneLine INNER JOIN Book ON OneLine.isbn = Book.isbn " +
        "ORDER BY OneLine.createdAt DESC LIMIT :pageSize OFFSET :pageIdx * :pageSize"
    )
    suspend fun getOneLineList(pageIdx : Int, pageSize : Int) : List<OneLineAndBookInfo>
}