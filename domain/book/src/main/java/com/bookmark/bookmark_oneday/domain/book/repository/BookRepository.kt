package com.bookmark.bookmark_oneday.domain.book.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.domain.book.model.BookDetail
import com.bookmark.bookmark_oneday.domain.book.model.BookItem
import com.bookmark.bookmark_oneday.domain.book.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistoryWithBook
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook

interface BookRepository  {
    suspend fun getBookList(perPage : Int = 30, key : String, sortType : String = "latest") : BaseResponse<PagingData<BookItem>>
    suspend fun getMyLibraryList(perPage : Int = 30, key : String, sortType : String = "latest") : BaseResponse<PagingData<MyLibraryItem.Book>>
    suspend fun getBookDetail(bookId : String) : BaseResponse<BookDetail>
    suspend fun removeBook(bookId : String) : BaseResponse<Nothing>
    suspend fun removeAllBook() : BaseResponse<Nothing>
    suspend fun updateReadingPage(bookId : String, currentPage : Int, totalPage : Int) : BaseResponse<Nothing>
    suspend fun deleteHistoryItem(bookId : String, targetId : String) : BaseResponse<ReadingInfo>
    suspend fun deleteHistoryAll(bookId: String) : BaseResponse<ReadingInfo>
    suspend fun getReadingInfo(bookId : String) : BaseResponse<ReadingInfo>
    suspend fun updateHistory(bookId : String, time : Int) : BaseResponse<ReadingInfo>
    suspend fun registerBook(book: RecognizedBook) : BaseResponse<Nothing>
    suspend fun checkDuplicate(isbn : String) : BaseResponse<Nothing>
    suspend fun getReadingHistoryOfMonth(year : Int, month : Int) : BaseResponse<List<ReadingHistory>>
    suspend fun getReadingHistoryWithBookOfDay(year : Int, month : Int, day : Int) : BaseResponse<List<ReadingHistoryWithBook>>
    suspend fun updateBookLike(bookId : String, like : Boolean) : BaseResponse<Boolean>
}