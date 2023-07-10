package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.domain.book.model.BookItem
import com.bookmark.bookmark_oneday.domain.book.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseGetBookList @Inject constructor(
    private val repository : BookRepository,
) {
    suspend operator fun invoke(key : String, sort : String = "latest") : BaseResponse<PagingData<MyLibraryItem.Book>> {
        return repository.getMyLibraryList(key = key, sortType = sort)
    }

    // 처음 3 페이지를 로드합니다.
    suspend fun loadInitPages(sort: String = "latest"): BaseResponse<PagingData<MyLibraryItem.Book>> {
        val firstResponse = repository.getMyLibraryList(key = "0", sortType = sort)
        if (firstResponse !is BaseResponse.Success<PagingData<MyLibraryItem.Book>>) return firstResponse

        val secondResponse = repository.getMyLibraryList(key = firstResponse.data.nextPageToken, sortType = sort)
        if (secondResponse !is BaseResponse.Success<PagingData<MyLibraryItem.Book>>) return secondResponse

        val lastResponse = repository.getMyLibraryList(key = secondResponse.data.nextPageToken, sortType = sort)


        if (lastResponse !is BaseResponse.Success<PagingData<MyLibraryItem.Book>>) return lastResponse
        val concatList = firstResponse.data.dataList + secondResponse.data.dataList + lastResponse.data.dataList

        return lastResponse.copy(data = lastResponse.data.copy(concatList))
    }

    suspend fun getPage(nextPageKey : String, sort : String = "latest") : BaseResponse<PagingData<BookItem>> {
        return repository.getBookList(key = nextPageKey, sortType = sort)
    }
}