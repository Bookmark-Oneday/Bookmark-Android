package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.write.model

import com.bookmark.bookmark_oneday.R

data class Font(
    val resourceId : Int, val title : String
) {
    companion object {
        const val FONT_DEFAULT = -1

        val defaultList = listOf(
            Font(FONT_DEFAULT, "default"),
            Font(R.font.yes24, "yes24"),
            Font(R.font.suit_regular, "suit"),
            Font(R.font.suit_bold, "suit-Bold")
        )

        fun getInstanceFromString(fontString : String) : Font {
            val font = defaultList.find { it.title == fontString }
            return font ?: Font(FONT_DEFAULT, "default")
        }
    }
}