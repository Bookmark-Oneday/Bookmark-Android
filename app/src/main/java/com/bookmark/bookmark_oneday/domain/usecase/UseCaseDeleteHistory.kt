package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

class UseCaseDeleteHistory @Inject constructor(
    private val repository: ReadingHistoryRepository
) {
    suspend operator fun invoke(bookId : String = "1", targetId : Int) : Boolean {
        val response = repository.deleteHistoryItem(bookId, targetId)
        return (response is BaseResponse.EmptySuccess)
    }

    suspend fun deleteAll() : Boolean {
        val response = repository.deleteHistoryAll("1")
        return (response is BaseResponse.EmptySuccess)
    }
}