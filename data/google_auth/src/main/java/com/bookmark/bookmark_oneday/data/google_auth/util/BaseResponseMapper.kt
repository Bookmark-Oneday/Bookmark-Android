package com.bookmark.bookmark_oneday.data.google_auth.util

import com.bookmark.bookmark_oneday.core.model.BaseResponse

fun <T, K> mapBaseResponse(response : BaseResponse<T>, mapper : (T) -> K) : BaseResponse<K> {
    return when (response) {
        is BaseResponse.Success -> {
            BaseResponse.Success(mapper(response.data))
        }
        is BaseResponse.Failure -> {
            response
        }
        is BaseResponse.EmptySuccess -> {
            response
        }
    }
}
