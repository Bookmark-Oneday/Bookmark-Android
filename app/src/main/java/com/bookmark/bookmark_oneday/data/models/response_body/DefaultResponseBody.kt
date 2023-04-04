package com.bookmark.bookmark_oneday.data.models.response_body

data class DefaultResponseBody<T, G>(
    val data : T,
    val meta : G? = null
)