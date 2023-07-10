package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookItem
import com.bookmark.bookmark_oneday.core.model.PagingData

interface BookRepository  {
    suspend fun getBookList(perPage : Int = 30, key : String, sortType : String = "latest") : BaseResponse<PagingData<BookItem>>
}