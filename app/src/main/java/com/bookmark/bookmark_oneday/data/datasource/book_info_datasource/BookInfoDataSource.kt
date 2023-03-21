package com.bookmark.bookmark_oneday.data.datasource.book_info_datasource

import com.bookmark.bookmark_oneday.data.models.KakaoBookResponse
import retrofit2.Response

interface BookInfoDataSource {
    suspend fun getBookInfo(isbn : String) : Response<KakaoBookResponse>
}