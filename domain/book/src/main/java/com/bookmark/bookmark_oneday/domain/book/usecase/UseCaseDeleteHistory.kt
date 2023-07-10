package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseDeleteHistory @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId : String, targetId : String) : BaseResponse<ReadingInfo> {
        return repository.deleteHistoryItem(bookId, targetId)
    }

    suspend fun deleteAll(bookId : String) : BaseResponse<ReadingInfo> {
        return repository.deleteHistoryAll(bookId)
    }
}