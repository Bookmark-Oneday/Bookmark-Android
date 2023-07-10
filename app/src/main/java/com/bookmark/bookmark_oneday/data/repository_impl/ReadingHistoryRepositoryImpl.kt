package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.data.models.dto.BookTimerDto
import com.bookmark.bookmark_oneday.data.utils.mapBaseResponse
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
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
        val response = bookDataSource.getBookTimer(bookId = bookId)
        return mapBaseResponse(response, BookTimerDto::toReadingInfo)
    }

    override suspend fun updateHistory(bookId: String, time: Int): BaseResponse<ReadingInfo> {
        val response = bookDataSource.updateReadingHistory(bookId = bookId, readingTime = time)
        return mapBaseResponse(response, BookTimerDto::toReadingInfo)
    }
}