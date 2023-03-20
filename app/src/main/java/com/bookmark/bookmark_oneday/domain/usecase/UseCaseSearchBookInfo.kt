package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.data.repository_impl.SearchBookDetailRepositoryImpl
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.repository.SearchBookDetailRepository

class UseCaseSearchBookInfo(
    private val searchBookDetailRepository: SearchBookDetailRepository = SearchBookDetailRepositoryImpl()
) {
    suspend operator fun invoke(isbn: String): BaseResponse<RecognizedBook> {
        return searchBookDetailRepository.searchAndGetBookDetail(isbn)
    }
}

