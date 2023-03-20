package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook

interface RegisterBookRepository {
    suspend fun registerBook(book: RecognizedBook) : BaseResponse<Nothing>
    suspend fun checkDuplicate(isbn : String) : BaseResponse<Nothing>
}