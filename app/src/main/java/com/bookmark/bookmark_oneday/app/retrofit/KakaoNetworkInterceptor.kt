package com.bookmark.bookmark_oneday.app.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class KakaoNetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder : Request.Builder = chain.request().newBuilder()
        val restApiKey = "__"
        builder.addHeader("Authorization", "KakaoAK $restApiKey")
        return chain.proceed(builder.build())
    }
}