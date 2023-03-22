package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.model.PagingData
import com.bookmark.bookmark_oneday.domain.repository.MyLibraryRepository
import kotlinx.coroutines.delay

class MyLibraryRepositoryImpl : MyLibraryRepository {
    override suspend fun getBookList(
        perPage: Int,
        key: String,
        sortType: String
    ): BaseResponse<PagingData<MyLibraryItem.Book>> {
        delay(300L)

        val isFinish = key.toIntOrNull() == 5 || key.toIntOrNull() == null
        if (isFinish) {
            return BaseResponse.Success(PagingData(dataList = listOf(), nextPageToken = "", totalItemCount = 150, isFinish = true))
        }

        val nextPageToken = key.toInt()
        val tempDataList = List(perPage){ MyLibraryItem.Book(it + perPage * nextPageToken, "", "${it + perPage * nextPageToken} 번째 책", "${it + perPage * nextPageToken}번째 지구") }
        val pagingData = PagingData(dataList = tempDataList, nextPageToken = (nextPageToken + 1).toString(), totalItemCount = 150)


        return BaseResponse.Success(pagingData)
    }
}