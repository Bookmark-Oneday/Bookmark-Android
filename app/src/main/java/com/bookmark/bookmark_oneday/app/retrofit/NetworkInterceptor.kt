package com.bookmark.bookmark_oneday.app.retrofit

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.SocketTimeoutException

class NetworkInterceptor(
    private val getToken : () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder : Request.Builder = request.newBuilder()
        val jwtToken : String ?= getToken()
        if (jwtToken != null) {
            builder.addHeader("user_id", "$jwtToken")
        }

        try {
            return chain.proceed(builder.build())
        } catch (e: Exception) {
            val msg: String
            val errorCode: Int

            when (e) {
                is SocketTimeoutException -> {
                    msg = "timeout"
                    errorCode = 408
                }
                else -> {
                    msg = "${e.message}"
                    errorCode = -1
                }
            }

            return Response.Builder()
                .request(request = request)
                .code(errorCode)
                .message(msg)
                .protocol(Protocol.HTTP_1_1)
                .body("{${e}}".toResponseBody(null))
                .build()
        }

    }
}