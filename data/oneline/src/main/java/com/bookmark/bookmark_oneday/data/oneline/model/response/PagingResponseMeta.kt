package com.bookmark.bookmark_oneday.data.oneline.model.response

data class PagingResponseMeta(
    val sortType : String,
    val continuousToken : String,
    val currentPage : Int,
    val totalCount : Int,
    val requestId : String,
    val now : String
)