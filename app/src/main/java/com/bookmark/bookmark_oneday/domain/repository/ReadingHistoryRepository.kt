package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.domain.model.BaseResponse

interface ReadingHistoryRepository {
    suspend fun deleteHistoryItem(targetId : Int) : BaseResponse<Nothing>
    suspend fun deleteHistoryAll() : BaseResponse<Nothing>
}