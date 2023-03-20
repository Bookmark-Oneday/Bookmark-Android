package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.data.repository_impl.RegisterBookRepositoryImpl
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.repository.RegisterBookRepository

class UseCaseRegisterBook(
    private val repository: RegisterBookRepository = RegisterBookRepositoryImpl()
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