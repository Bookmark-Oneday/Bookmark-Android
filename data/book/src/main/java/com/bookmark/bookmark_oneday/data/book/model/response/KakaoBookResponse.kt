package com.bookmark.bookmark_oneday.data.book.model.response

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
) {
    // 카카오 책 인식 api 에서, isbn 값이 2개가 나오는 경우가 있다.
    fun getSingleIsbn() : String {
        return try {
            val isbnList = isbn.split(" ")
            isbnList.last()
        } catch (e : Exception) {
            isbn
        }
    }
}

data class KakaoBookMeta(
    val is_end : Boolean,
    val pageable_count : Int,
    val total_count : Int
)
