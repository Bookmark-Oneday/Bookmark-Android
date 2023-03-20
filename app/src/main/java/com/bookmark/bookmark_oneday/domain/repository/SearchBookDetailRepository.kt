package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook

interface SearchBookDetailRepository {
    suspend fun searchAndGetBookDetail(isbn : String) : BaseResponse<RecognizedBook>
}