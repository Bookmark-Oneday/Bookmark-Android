package com.bookmark.bookmark_oneday.data.models.response_meta

data class PagingResponseMeta(
    val sortType : String,
    val continuousToken : String,
    val currentPage : Int,
    val totalCount : Int,
    val requestId : String,
    val now : String
)