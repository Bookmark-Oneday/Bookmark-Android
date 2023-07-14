package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseDeleteBook @Inject constructor(
    private val repository : BookRepository
) {
    suspend operator fun invoke(bookId : String) : Boolean {
        val response = repository.removeBook(bookId)
        return response is BaseResponse.EmptySuccess
    }
}