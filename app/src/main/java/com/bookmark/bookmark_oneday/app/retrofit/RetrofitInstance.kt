package com.bookmark.bookmark_oneday.app.retrofit

import com.bookmark.bookmark_oneday.data.datasource.token_datasource.TokenDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://test"
    private lateinit var retrofit : Retrofit

    fun init(tokenDataSource: TokenDataSource) {
        if (!::retrofit.isInitialized) {
            val okhttpClient = getOkHttpClient(tokenDataSource)

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    private fun getOkHttpClient(tokenDataSource: TokenDataSource): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(5000L, TimeUnit.MILLISECONDS)
            .addInterceptor(NetworkInterceptor(tokenDataSource::accessToken))
            .build()
    }

    fun getInstance() : Retrofit {
        return retrofit
    }
}