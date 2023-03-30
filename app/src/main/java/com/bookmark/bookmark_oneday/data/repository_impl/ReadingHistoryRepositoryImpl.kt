package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class ReadingHistoryRepositoryImpl @Inject constructor(

) : ReadingHistoryRepository {
    override suspend fun deleteHistoryItem(targetId: Int): BaseResponse<Nothing> {
        delay(1000L)
        return BaseResponse.EmptySuccess
    }

    override suspend fun deleteHistoryAll(): BaseResponse<Nothing> {
        delay(1000L)
        return BaseResponse.EmptySuccess
    }
}