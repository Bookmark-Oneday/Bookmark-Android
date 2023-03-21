package com.bookmark.bookmark_oneday.domain.model

sealed class BaseResponse<out T> {
    data class Success<T>(
        val data : T,
    ) : BaseResponse<T>()

    data class Failure(
        val errorCode : Int,
        val errorMessage : String
    ) : BaseResponse<Nothing>()

    object EmptySuccess : BaseResponse<Nothing>()
}
