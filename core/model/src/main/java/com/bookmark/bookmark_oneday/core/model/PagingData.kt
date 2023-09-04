package com.bookmark.bookmark_oneday.core.model

data class PagingData<out T>(
    val dataList : List<T>,
    val nextPageToken : String,
    val totalItemCount : Int,
    val isFinish : Boolean = false
)
