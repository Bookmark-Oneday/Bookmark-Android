package com.bookmark.bookmark_oneday.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bookmark.bookmark_oneday.core.room.entity.OneLineEntity

@Dao
interface OneLineDao {
    @Insert
    suspend fun insertOneLine(oneLine: OneLineEntity)

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
        "SELECT OneLineEntity.id AS id, OneLineEntity.isbn AS isbn, BookEntity.title AS title, BookEntity.authors AS authors, content AS oneliner, " +
                "OneLineEntity.color AS color, OneLineEntity.centerX AS leftPosition, OneLineEntity.centerY AS topPosition, " +
                "OneLineEntity.font AS font, OneLineEntity.fontSize AS font_size, OneLineEntity.backgroundUri AS bg_image_url, OneLineEntity.createdAt as created_at " +
                "FROM OneLineEntity INNER JOIN BookEntity ON OneLineEntity.isbn = BookEntity.isbn " +
                "ORDER BY OneLineEntity.createdAt DESC LIMIT :pageSize OFFSET :pageIdx * :pageSize"
    )
    suspend fun getOneLineList(pageIdx : Int, pageSize : Int) : List<OneLineAndBookInfo>

    @Query("DELETE FROM OneLineEntity WHERE id = :id")
    suspend fun deleteOneline(id : String)
}