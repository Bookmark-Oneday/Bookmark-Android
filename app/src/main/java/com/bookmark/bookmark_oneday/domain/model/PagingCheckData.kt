package com.bookmark.bookmark_oneday.domain.model

data class PagingCheckData(
    var lastLoadedKey : String ?= null,
    var nextKey : String ?= null,
    var tryLoading : Boolean = true,
    var isFinish : Boolean = false
) {
    fun setRefresh() {
        lastLoadedKey = null
        nextKey = null
        tryLoading = true
        isFinish = false
    }

    fun setLoadSuccess(nextKey : String) {
        lastLoadedKey = this.nextKey
        this.nextKey = nextKey
        tryLoading = false
        isFinish = false
    }

    fun setFinish() {
        nextKey = null
        tryLoading = false
        isFinish = true
    }

    fun setLoading() {
        tryLoading = true
    }

    // 로드 실패시 상태 변화는 없습니다.
    fun setLoadFail() {  }

}
