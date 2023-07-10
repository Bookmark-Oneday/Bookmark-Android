package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.EditPageRepository
import javax.inject.Inject

class UseCaseEditReadingPage @Inject constructor(
   private val repository : EditPageRepository
) {
    suspend operator fun invoke(bookId : String, currentPage : Int, totalPage : Int) : Boolean {
        val response = repository.updateReadingPage(bookId, currentPage, totalPage)
        return response is BaseResponse.EmptySuccess
    }
}