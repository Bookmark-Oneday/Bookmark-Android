package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model

data class Font(
    val resourceId : Int, val title : String
) {
    companion object {
        val defaultList = listOf(
            Font(0, "기본"),
            Font(1, "yes24"),
            Font(2, "에스코어 드립"),
            Font(3, "기본"),
            Font(4, "yes24"),
            Font(5, "에스코어 드립")
        )
    }
}