package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseEditReadingPage @Inject constructor(
   private val repository : BookRepository
) {
    suspend operator fun invoke(bookId : String, currentPage : Int, totalPage : Int) : Boolean {
        val response = repository.updateReadingPage(bookId, currentPage, totalPage)
        return response is BaseResponse.EmptySuccess
    }
}