package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import javax.inject.Inject

class UseCaseSaveReadingHistory @Inject constructor(
    private val repository : ReadingHistoryRepository
) {
    suspend operator fun invoke(bookId: String, time: Int): BaseResponse<ReadingInfo> {
        return repository.updateHistory(bookId, time)
    }
}