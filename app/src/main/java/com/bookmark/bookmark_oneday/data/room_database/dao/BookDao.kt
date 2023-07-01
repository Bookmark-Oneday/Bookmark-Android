package com.bookmark.bookmark_oneday.data.room_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bookmark.bookmark_oneday.data.models.dto.BookItemDto
import com.bookmark.bookmark_oneday.data.models.dto.HistoryDto
import com.bookmark.bookmark_oneday.data.room_database.entity.Book
import com.bookmark.bookmark_oneday.data.room_database.entity.ReadingHistory
import com.bookmark.bookmark_oneday.data.room_database.entity.RegisteredBook

@Dao
interface BookDao {
    @Query(
        "SELECT registeredBook.id AS book_id, book.title AS title, " +
        "authors, translators, publisher, book.backgroundUri AS titleImage, reading, favorite " +
        "FROM registeredBook " +
        "INNER JOIN book ON book.isbn = registeredBook.isbn " +
        "ORDER BY registeredBook.registeredAt DESC LIMIT :pageSize OFFSET :pageIdx * :pageSize"
    )
    suspend fun getBookItemList(pageIdx: Int, pageSize: Int) : List<BookItemDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegisteredBook(registeredBook: RegisteredBook)

    @Query("SELECT * FROM registeredBook WHERE id = :bookId")
    suspend fun getRegisteredBook(bookId : Int) : List<RegisteredBook>

    @Query("DELETE FROM registeredBook WHERE id = :bookId")
    suspend fun deleteRegisteredBook(bookId : Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book : Book)

    @Query("SELECT * FROM book WHERE isbn = :isbn")
    suspend fun getBook(isbn : String) : List<Book>

    @Query("SELECT COUNT(*) FROM book WHERE isbn = :isbn")
    suspend fun getBookCount(isbn : String) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadingHistory(readingHistory: ReadingHistory)

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