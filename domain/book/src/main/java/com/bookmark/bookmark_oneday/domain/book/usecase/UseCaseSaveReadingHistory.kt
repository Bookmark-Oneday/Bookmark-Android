package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseSaveReadingHistory @Inject constructor(
    private val repository : BookRepository
) {
    suspend operator fun invoke(bookId: String, time: Int): BaseResponse<ReadingInfo> {
        return repository.updateHistory(bookId, time)
    }
}