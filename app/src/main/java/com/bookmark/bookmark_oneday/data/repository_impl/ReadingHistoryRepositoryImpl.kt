package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.data.models.dto.BookTimerDto
import com.bookmark.bookmark_oneday.data.utils.mapBaseResponse
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

class ReadingHistoryRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
) : ReadingHistoryRepository {
    override suspend fun deleteHistoryItem(bookId : String, targetId: String): BaseResponse<ReadingInfo> {
        val response = bookDataSource.deleteReadingHistory(bookId, targetId)
        return mapBaseResponse(response, BookTimerDto::toReadingInfo)
    }

    override suspend fun deleteHistoryAll(bookId: String): BaseResponse<ReadingInfo> {
        val response = bookDataSource.deleteReadingHistory(bookId)
        return mapBaseResponse(response, BookTimerDto::toReadingInfo)
    }

    override suspend fun getReadingInfo(bookId: String): BaseResponse<ReadingInfo> {
        val response = bookDataSource.getBookTimer(book_id = bookId)
        return mapBaseResponse(response, BookTimerDto::toReadingInfo)
    }

    override suspend fun updateHistory(bookId: String, time: Int): BaseResponse<ReadingInfo> {
        val response = bookDataSource.updateReadingHistory(book_id = bookId, reading_time = time)
        return mapBaseResponse(response, BookTimerDto::toReadingInfo)
    }
}