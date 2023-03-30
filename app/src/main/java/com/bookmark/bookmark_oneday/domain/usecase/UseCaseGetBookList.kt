package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.data.repository_impl.MyLibraryRepositoryImpl
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.model.PagingData
import com.bookmark.bookmark_oneday.domain.repository.MyLibraryRepository

class UseCaseGetBookList constructor(
    private val repository : MyLibraryRepository = MyLibraryRepositoryImpl()
) {
    suspend operator fun invoke(key : String, sort : String = "latest") : BaseResponse<PagingData<MyLibraryItem.Book>> {
        return repository.getBookList(key = key, sortType = sort)
    }

    // 처음 3 페이지를 로드합니다.
    suspend fun loadInitPages(sort: String = "latest"): BaseResponse<PagingData<MyLibraryItem.Book>> {
        val firstResponse = repository.getBookList(key = "0", sortType = sort)
        if (firstResponse !is BaseResponse.Success<PagingData<MyLibraryItem.Book>>) return firstResponse

        val secondResponse = repository.getBookList(key = firstResponse.data.nextPageToken, sortType = sort)
        if (secondResponse !is BaseResponse.Success<PagingData<MyLibraryItem.Book>>) return secondResponse

        val lastResponse = repository.getBookList(key = secondResponse.data.nextPageToken, sortType = sort)


        if (lastResponse !is BaseResponse.Success<PagingData<MyLibraryItem.Book>>) return lastResponse
        val concatList = firstResponse.data.dataList + secondResponse.data.dataList + lastResponse.data.dataList

        return lastResponse.copy(data = lastResponse.data.copy(concatList))
    }
}