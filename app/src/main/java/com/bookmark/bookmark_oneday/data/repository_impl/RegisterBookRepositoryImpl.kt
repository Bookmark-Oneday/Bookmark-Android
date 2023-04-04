package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.repository.RegisterBookRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class RegisterBookRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
) : RegisterBookRepository {
    override suspend fun registerBook(book: RecognizedBook): BaseResponse<Nothing> {
        return bookDataSource.registerBook(book)
    }

    // todo 중복 검사 api 정의되면 수정
    override suspend fun checkDuplicate(isbn: String): BaseResponse<Nothing> {
        delay(1000L)
        return BaseResponse.Failure(errorCode = 409, errorMessage = "exist book isbn")
    }
}