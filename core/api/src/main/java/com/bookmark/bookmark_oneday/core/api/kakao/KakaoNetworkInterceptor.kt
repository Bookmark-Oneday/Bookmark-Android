package com.bookmark.bookmark_oneday.core.api.kakao

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class KakaoNetworkInterceptor(
    private val kakaoRestApiKey : String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder : Request.Builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "KakaoAK $kakaoRestApiKey")
        return chain.proceed(builder.build())
    }
}