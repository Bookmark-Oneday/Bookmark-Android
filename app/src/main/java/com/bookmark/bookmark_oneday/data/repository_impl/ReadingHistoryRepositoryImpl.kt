package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

class ReadingHistoryRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
) : ReadingHistoryRepository {
    // todo EmptySuccess 에서 BaseResponse.Success<List<HistoryItem>> 으로 수정
    override suspend fun deleteHistoryItem(bookId : String, targetId: String): BaseResponse<Nothing> {
        val response = bookDataSource.deleteReadingHistory(bookId, targetId)

        return if (response is BaseResponse.Failure) response
        else BaseResponse.EmptySuccess
    }

    override suspend fun deleteHistoryAll(bookId: String): BaseResponse<Nothing> {
        val response = bookDataSource.deleteReadingHistory(bookId)

        return if (response is BaseResponse.Failure) response
        else BaseResponse.EmptySuccess
    }
}