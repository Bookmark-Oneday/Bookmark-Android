package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseRegisterBook @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: RecognizedBook) : BaseResponse<Nothing>{
        return repository.registerBook(book)
    }

    suspend fun withDuplicationCheck(book: RecognizedBook) : BaseResponse<Nothing> {
        val duplicateResponse = repository.checkDuplicate(book.isbn)

        if (duplicateResponse is BaseResponse.Failure) {
            return duplicateResponse
        }

        return repository.registerBook(book)
    }
}