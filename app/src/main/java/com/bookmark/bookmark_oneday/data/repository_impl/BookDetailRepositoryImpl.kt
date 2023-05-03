package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.data.models.dto.BookDetailDto
import com.bookmark.bookmark_oneday.data.utils.mapBaseResponse
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookDetail
import com.bookmark.bookmark_oneday.domain.repository.BookDetailRepository
import javax.inject.Inject

class BookDetailRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
) : BookDetailRepository {
    override suspend fun getBookDetail(bookId: String): BaseResponse<BookDetail> {
        val response = bookDataSource.getBookDetail(bookId = bookId)
        return mapBaseResponse(response = response, BookDetailDto::toBookDetail)
    }
}