package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.domain.model.BaseResponse

interface ReadingHistoryRepository {
    suspend fun deleteHistoryItem(bookId : String, targetId : String) : BaseResponse<Nothing>
    suspend fun deleteHistoryAll(bookId: String) : BaseResponse<Nothing>
}