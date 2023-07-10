package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.ReadingInfo

interface ReadingHistoryRepository {
    suspend fun deleteHistoryItem(bookId : String, targetId : String) : BaseResponse<ReadingInfo>
    suspend fun deleteHistoryAll(bookId: String) : BaseResponse<ReadingInfo>
    suspend fun getReadingInfo(bookId : String) : BaseResponse<ReadingInfo>
    suspend fun updateHistory(bookId : String, time : Int) : BaseResponse<ReadingInfo>
}