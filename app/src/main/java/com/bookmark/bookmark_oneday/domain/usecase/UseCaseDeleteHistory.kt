package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

class UseCaseDeleteHistory @Inject constructor(
    private val repository: ReadingHistoryRepository
) {
    suspend operator fun invoke(bookId : String, targetId : String) : BaseResponse<ReadingInfo> {
        return repository.deleteHistoryItem(bookId, targetId)
    }

    suspend fun deleteAll(bookId : String) : BaseResponse<ReadingInfo> {
        return repository.deleteHistoryAll(bookId)
    }
}