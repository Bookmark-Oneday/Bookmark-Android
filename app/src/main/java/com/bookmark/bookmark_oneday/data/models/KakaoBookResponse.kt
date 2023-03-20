package com.bookmark.bookmark_oneday.data.models

data class KakaoBookResponse(
    val documents : List<KakaoBookDetail>,
    val meta : KakaoBookMeta
)

data class KakaoBookDetail(
    val authors : List<String>,
    val contents : String,
    val datetime : String,
    val isbn : String,
    val price : Int,
    val publisher : String,
    val sale_price : Int,
    val status : String,
    val thumbnail : String,
    val title : String,
    val translators : List<String>,
    val url : String
)

data class KakaoBookMeta(
    val is_end : Boolean,
    val pageable_count : Int,
    val total_count : Int
)
