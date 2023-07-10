package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.repository.SearchBookDetailRepository
import javax.inject.Inject

class UseCaseSearchBookInfo @Inject constructor(
    private val searchBookDetailRepository: SearchBookDetailRepository
) {
    suspend operator fun invoke(isbn: String): BaseResponse<RecognizedBook> {
        return searchBookDetailRepository.searchAndGetBookDetail(isbn)
    }
}

