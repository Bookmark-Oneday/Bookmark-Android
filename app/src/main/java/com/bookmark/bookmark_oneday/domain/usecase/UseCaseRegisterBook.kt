package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.repository.RegisterBookRepository
import javax.inject.Inject

class UseCaseRegisterBook @Inject constructor(
    private val repository: RegisterBookRepository
) {
    suspend operator fun invoke(book: RecognizedBook) : BaseResponse<Nothing> {
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