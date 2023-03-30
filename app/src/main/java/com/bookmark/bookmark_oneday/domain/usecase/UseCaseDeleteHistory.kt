package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

class UseCaseDeleteHistory @Inject constructor(
    private val repository: ReadingHistoryRepository
) {
    suspend operator fun invoke(targetId : Int) : Boolean {
        val response = repository.deleteHistoryItem(targetId)
        return (response is BaseResponse.EmptySuccess)
    }

    suspend fun deleteAll() : Boolean {
        val response = repository.deleteHistoryAll()
        return (response is BaseResponse.EmptySuccess)
    }
}