package com.bookmark.bookmark_oneday.core.api.google

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GoogleLoginRetrofitInstance {
    private const val GOOGLE_BASE_URL = "https://oauth2.googleapis.com"
    private lateinit var retrofit : Retrofit

    fun init() {
        if (!GoogleLoginRetrofitInstance::retrofit.isInitialized) {
            val okhttpClient = getOkHttpClient()

            retrofit = Retrofit.Builder()
                .baseUrl(GOOGLE_BASE_URL)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .writeTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(5000L, TimeUnit.MILLISECONDS)
            .build()
    }

    fun getInstance() : Retrofit {
        return retrofit
    }

}