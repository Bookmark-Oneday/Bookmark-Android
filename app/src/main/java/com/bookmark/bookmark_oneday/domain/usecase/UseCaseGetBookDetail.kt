package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookDetail
import com.bookmark.bookmark_oneday.domain.repository.BookDetailRepository
import javax.inject.Inject

class UseCaseGetBookDetail @Inject constructor(
    private val bookDetailRepository: BookDetailRepository
) {
    suspend fun invoke(bookId : String) : BaseResponse<BookDetail> {
        return bookDetailRepository.getBookDetail(bookId = bookId)
    }
}