package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.DeleteBookRepository
import javax.inject.Inject

class UseCaseDeleteBook @Inject constructor(
    private val repository : DeleteBookRepository
) {
    suspend operator fun invoke(bookId : String) : Boolean {
        val response = repository.removeBook(bookId)
        return response is BaseResponse.EmptySuccess
    }
}