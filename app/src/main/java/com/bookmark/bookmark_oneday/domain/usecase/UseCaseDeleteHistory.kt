package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.data.repository_impl.ReadingHistoryRepositoryImpl
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository

class UseCaseDeleteHistory(
    private val repository: ReadingHistoryRepository = ReadingHistoryRepositoryImpl()
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