package com.bookmark.bookmark_oneday.data.datasource.book_datasource

import com.bookmark.bookmark_oneday.data.models.dto.BookDetailDto
import com.bookmark.bookmark_oneday.data.models.dto.BookItemDto
import com.bookmark.bookmark_oneday.data.models.dto.BookTimerDto
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.PagingData
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook

interface BookDataSource {
    suspend fun registerBook(recognizedBook: RecognizedBook) : BaseResponse<Nothing>
    suspend fun getBookDetail(bookId : String) : BaseResponse<BookDetailDto>
    suspend fun getBookList(perPage : Int, continuousToken : String, sortType : String) : BaseResponse<PagingData<BookItemDto>>
    suspend fun deleteBook(bookId : String) : BaseResponse<Nothing>

    suspend fun updateReadingPage(bookId : String, currentPage : Int, totalPage : Int) : BaseResponse<Nothing>
    suspend fun getBookTimer(bookId : String) : BaseResponse<BookTimerDto>
    suspend fun updateReadingHistory(bookId : String, readingTime : Int) : BaseResponse<BookTimerDto>
    suspend fun deleteReadingHistory(bookId : String, historyId : String? = null) : BaseResponse<BookTimerDto>
}