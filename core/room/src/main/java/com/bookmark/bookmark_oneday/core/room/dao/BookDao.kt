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
import com.bookmark.bookmark_oneday.core.room.model.HistoryWithBookDto

@Dao
interface BookDao {
    @Query(
        "SELECT registeredBook.id AS book_id, bookEntity.title AS title, " +
        "authors, translators, publisher, bookEntity.backgroundUri AS titleImage, reading, favorite " +
        "FROM registeredBook " +
        "INNER JOIN bookEntity ON bookEntity.isbn = registeredBook.isbn " +
        "ORDER BY " +
            "CASE WHEN :sort = 'latest' THEN registeredBook.registeredAt END DESC," +
            "CASE WHEN :sort = 'past' THEN registeredBook.registeredAt END ASC, " +
            "CASE WHEN :sort = 'favorite' THEN registeredBook.favorite END DESC," +
            "CASE WHEN :sort = 'favorite' THEN registeredBook.registeredAt END DESC," +
            "CASE WHEN :sort = 'reading' THEN registeredBook.reading END DESC, " +
            "CASE WHEN :sort = 'reading' THEN registeredBook.registeredAt END DESC " +
        "LIMIT :pageSize OFFSET :pageIdx * :pageSize"
    )
    suspend fun getBookItemList(pageIdx: Int, pageSize: Int, sort : String = "latest") : List<BookItemDto>

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

    @Query("SELECT COUNT(*) FROM registeredBook WHERE isbn = :isbn")
    suspend fun getBookCount(isbn : String) : Int

    @Query("SELECT COUNT(*) FROM registeredBook")
    suspend fun getAmountOfRegisteredBook() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingHistory(readingHistory: ReadingHistoryEntity)

    @Query("SELECT id, timeSec AS time, date FROM ReadingHistory WHERE bookId = :bookId")
    suspend fun getReadingHistoryList(bookId : Int) : List<HistoryDto>

    @Query("DELETE FROM readingHistory WHERE id = :historyId")
    suspend fun deleteReadingHistory(historyId : Int)

    @Query("DELETE FROM readingHistory WHERE bookId = :bookId")
    suspend fun deleteAllReadingHistoryOfBook(bookId : Int)

    @Query("UPDATE registeredBook SET currentPage = :currentPage, totalPage = :totalPage, reading = :currentPage > 0 WHERE id = :bookId")
    suspend fun updatePageInfo(bookId : Int, currentPage : Int, totalPage : Int) : Int

    @Query("SELECT COALESCE(SUM(timeSec), 0) FROM readingHistory WHERE date LIKE :dateQuery || '%' ")
    suspend fun getDailyTotalReadingTime(dateQuery : String) : Int

    @Query("SELECT isbn FROM registeredBook WHERE id = :bookId")
    suspend fun getIsbnByRegisteredBookId(bookId : Int) : String

    @Query("SELECT RegisteredBook.id AS bookId, timeSec AS time, date, BookEntity.title AS title, BookEntity.authors AS authors, BookEntity.backgroundUri AS titleImage FROM ReadingHistory " +
            "INNER JOIN RegisteredBook ON RegisteredBook.id == ReadingHistory.bookId " +
            "INNER JOIN BookEntity ON BookEntity.isbn == RegisteredBook.isbn " +
            "WHERE ReadingHistory.date LIKE :dateQuery || '%' ")
    suspend fun getReadingHistoryWithBookByDateQuery(dateQuery : String) : List<HistoryWithBookDto>

    @Query("SELECT id, timeSec AS time, date FROM ReadingHistory WHERE ReadingHistory.date LIKE :dateQuery || '%' ")
    suspend fun getReadingHistoryByDateQuery(dateQuery : String) : List<HistoryDto>

}