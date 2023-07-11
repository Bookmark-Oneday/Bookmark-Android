package com.bookmark.bookmark_oneday.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bookmark.bookmark_oneday.core.room.entity.BookEntity
import com.bookmark.bookmark_oneday.core.room.entity.ReadingHistoryEntity
import com.bookmark.bookmark_oneday.core.room.entity.RegisteredBookEntity
import com.bookmark.bookmark_oneday.core.room.model.BookItemDto
import com.bookmark.bookmark_oneday.core.room.model.HistoryDto

@Dao
interface BookDao {
    @Query(
        "SELECT registeredBook.id AS book_id, bookEntity.title AS title, " +
        "authors, translators, publisher, bookEntity.backgroundUri AS titleImage, reading, favorite " +
        "FROM registeredBook " +
        "INNER JOIN bookEntity ON bookEntity.isbn = registeredBook.isbn " +
        "ORDER BY registeredBook.registeredAt DESC LIMIT :pageSize OFFSET :pageIdx * :pageSize"
    )
    suspend fun getBookItemList(pageIdx: Int, pageSize: Int) : List<BookItemDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegisteredBook(registeredBook: RegisteredBookEntity)

    @Query("SELECT * FROM registeredBook WHERE id = :bookId")
    suspend fun getRegisteredBook(bookId : Int) : List<RegisteredBookEntity>

    @Query("DELETE FROM registeredBook WHERE id = :bookId")
    suspend fun deleteRegisteredBook(bookId : Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book : BookEntity)

    @Query("SELECT * FROM bookEntity WHERE isbn = :isbn")
    suspend fun getBook(isbn : String) : List<BookEntity>

    @Query("SELECT COUNT(*) FROM bookEntity WHERE isbn = :isbn")
    suspend fun getBookCount(isbn : String) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingHistory(readingHistory: ReadingHistoryEntity)

    @Query("SELECT id, timeSec AS time, date FROM ReadingHistory WHERE bookId = :bookId")
    suspend fun getReadingHistoryList(bookId : Int) : List<HistoryDto>

    @Query("DELETE FROM readingHistory WHERE id = :historyId")
    suspend fun deleteReadingHistory(historyId : Int)

    @Query("DELETE FROM readingHistory WHERE bookId = :bookId")
    suspend fun deleteAllReadingHistoryOfBook(bookId : Int)

    @Query("UPDATE registeredBook SET currentPage = :currentPage, totalPage = :totalPage WHERE id = :bookId")
    suspend fun updatePageInfo(bookId : Int, currentPage : Int, totalPage : Int) : Int

    @Query("SELECT COALESCE(SUM(timeSec), 0) FROM readingHistory WHERE date LIKE :dateQuery || '%' ")
    suspend fun getDailyTotalReadingTime(dateQuery : String) : Int

    @Query("SELECT isbn FROM registeredBook WHERE id = :bookId")
    suspend fun getIsbnByRegisteredBookId(bookId : Int) : String
}