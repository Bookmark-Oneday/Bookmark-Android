package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.BookDetail
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseGetBookDetail @Inject constructor(
    private val bookDetailRepository: BookRepository
) {
    suspend fun invoke(bookId : String) : BaseResponse<BookDetail> {
        return bookDetailRepository.getBookDetail(bookId = bookId)
    }
}