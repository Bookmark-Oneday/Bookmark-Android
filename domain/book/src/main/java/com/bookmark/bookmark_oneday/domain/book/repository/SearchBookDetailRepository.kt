package com.bookmark.bookmark_oneday.domain.book.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook

interface SearchBookDetailRepository {
    suspend fun searchAndGetBookDetail(isbn : String) : BaseResponse<RecognizedBook>
}