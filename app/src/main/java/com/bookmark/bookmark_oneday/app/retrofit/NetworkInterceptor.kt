package com.bookmark.bookmark_oneday.app.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkInterceptor(
    private val getToken : () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder : Request.Builder = chain.request().newBuilder()
        val jwtToken : String ?= getToken()
        if (jwtToken != null) {
            builder.addHeader("Authorization", "Bearer $jwtToken")
        }
        return chain.proceed(builder.build())
    }
}