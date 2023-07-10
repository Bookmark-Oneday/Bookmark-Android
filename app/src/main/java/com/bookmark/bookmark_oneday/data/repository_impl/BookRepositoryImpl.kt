package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.data.models.dto.BookItemDto
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookItem
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDataSource: BookDataSource
)  : BookRepository {
    override suspend fun getBookList(
        perPage: Int,
        key: String,
        sortType: String
    ): BaseResponse<PagingData<BookItem>> {
        val response = bookDataSource.getBookList(perPage = perPage, continuousToken = key, sortType = sortType)

        if (response is BaseResponse.Failure) {
            return response
        }
        if (response is BaseResponse.Success) {
            val mappingData = response.data.dataList.map { BookItemDto.toBookItem(it) }
            val pagingData = PagingData(
                dataList = mappingData,
                nextPageToken = response.data.nextPageToken,
                totalItemCount = response.data.totalItemCount,
                isFinish = response.data.isFinish
            )
            return BaseResponse.Success(pagingData)
        }

        throw IllegalAccessException("response must be BaseResponse.Failure or BaseResponse.Success")
    }
}