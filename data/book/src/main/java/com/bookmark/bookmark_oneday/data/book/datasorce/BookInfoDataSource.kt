package com.bookmark.bookmark_oneday.data.book.datasorce

import com.bookmark.bookmark_oneday.data.book.model.response.KakaoBookResponse
import retrofit2.Response

interface BookInfoDataSource {
    suspend fun getBookInfo(isbn : String) : Response<KakaoBookResponse>
}