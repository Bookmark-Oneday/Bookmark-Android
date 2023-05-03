package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.EditPageRepository
import javax.inject.Inject

class EditPageRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
) : EditPageRepository {
    override suspend fun updateReadingPage(bookId: String, currentPage: Int, totalPage: Int) : BaseResponse<Nothing> {
        return bookDataSource.updateReadingPage(bookId = bookId, currentPage = currentPage, totalPage = totalPage)
    }
}