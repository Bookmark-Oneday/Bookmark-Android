package com.bookmark.bookmark_oneday.app.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object KakaoRetrofitInstance {
    private const val BASE_URL = "https://dapi.kakao.com"
    private lateinit var retrofit : Retrofit

    fun init(apiKey : String) {
        if (!::retrofit.isInitialized) {
            val okhttpClient = getOkHttpClient(apiKey)

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    private fun getOkHttpClient(apiKey : String): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(5000L, TimeUnit.MILLISECONDS)
            .addInterceptor(KakaoNetworkInterceptor(apiKey))
            .build()
    }

    fun getInstance() : Retrofit {
        return retrofit
    }
}