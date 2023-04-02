package com.bookmark.bookmark_oneday.data.datasource.book_datasource

import com.bookmark.bookmark_oneday.data.models.dto.BookDetailDto
import com.bookmark.bookmark_oneday.data.models.dto.BookItemDto
import com.bookmark.bookmark_oneday.data.models.dto.BookTimerDto
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.PagingData
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook

interface BookDataSource {
    suspend fun registerBook(recognizedBook: RecognizedBook) : BaseResponse<Nothing>

    suspend fun getBookDetail(book_id : String) : BaseResponse<BookDetailDto>
    suspend fun getBookList(perPage : Int, continuousToken : String, sortType : String) : BaseResponse<PagingData<BookItemDto>>
    suspend fun deleteBook(book_id : String) : BaseResponse<Nothing>

    suspend fun updateReadingPage(book_id : String, current_page : Int, total_page : Int) : BaseResponse<Nothing>
    suspend fun getBookTimer(book_id : String) : BaseResponse<BookTimerDto>
    suspend fun updateReadingHistory(book_id : String, reading_time : Int) : BaseResponse<BookTimerDto>
    suspend fun deleteReadingHistory(book_id : String, historyId : String? = null) : BaseResponse<BookTimerDto>
}