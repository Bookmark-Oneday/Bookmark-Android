package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.book.repository.SearchBookDetailRepository
import javax.inject.Inject

class UseCaseSearchBookInfo @Inject constructor(
    private val searchBookDetailRepository: SearchBookDetailRepository
) {
    suspend operator fun invoke(isbn: String): BaseResponse<RecognizedBook> {
        return searchBookDetailRepository.searchAndGetBookDetail(isbn)
    }
}

