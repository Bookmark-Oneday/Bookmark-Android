package com.bookmark.bookmark_oneday.data.book.datasorce

import com.bookmark.bookmark_oneday.core.api.di.KakaoHttpClient
import com.bookmark.bookmark_oneday.data.book.model.response.KakaoBookResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class KakaoBookInfoDataSource @Inject constructor(
    @KakaoHttpClient retrofit : Retrofit
): BookInfoDataSource {
    private val service = retrofit.create(KakaoBookSearchApi::class.java)

    override suspend fun getBookInfo(isbn: String): Response<KakaoBookResponse> {
        return service.getBookDetail(query = isbn)
    }
}

interface KakaoBookSearchApi {
    @GET("/v3/search/book")
    suspend fun getBookDetail(@Query("query") query : String, @Query("target") target : String = "isbn") : Response<KakaoBookResponse>
}