package com.bookmark.bookmark_oneday.data.oneline.model.response

data class DefaultResponseBody<T, G>(
    val data : T,
    val meta : G? = null
)