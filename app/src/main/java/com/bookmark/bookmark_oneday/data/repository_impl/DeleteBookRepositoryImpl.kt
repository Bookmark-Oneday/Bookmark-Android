package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.repository.DeleteBookRepository
import javax.inject.Inject

class DeleteBookRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
) : DeleteBookRepository {
    override suspend fun removeBook(bookId: String): BaseResponse<Nothing> {
        return bookDataSource.deleteBook(bookId)
    }

    // todo 전체 책 삭제 api 준비되면 해당 api 에 맞게 수정
    override suspend fun removeAllBook(): BaseResponse<Nothing> {
        return bookDataSource.deleteBook("1")
    }

}