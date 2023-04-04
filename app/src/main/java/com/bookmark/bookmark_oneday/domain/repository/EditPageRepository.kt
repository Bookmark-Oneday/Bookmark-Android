package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.domain.model.BaseResponse

interface EditPageRepository {
    suspend fun updateReadingPage(bookId : String, currentPage : Int, totalPage : Int) : BaseResponse<Nothing>
}