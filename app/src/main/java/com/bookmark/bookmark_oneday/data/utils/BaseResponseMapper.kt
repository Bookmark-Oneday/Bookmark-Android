package com.bookmark.bookmark_oneday.data.utils

import com.bookmark.bookmark_oneday.domain.model.BaseResponse

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
