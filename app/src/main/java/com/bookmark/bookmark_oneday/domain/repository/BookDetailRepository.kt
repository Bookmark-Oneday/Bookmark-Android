package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookDetail

interface BookDetailRepository {
    suspend fun getBookDetail(bookId : String) : BaseResponse<BookDetail>
}