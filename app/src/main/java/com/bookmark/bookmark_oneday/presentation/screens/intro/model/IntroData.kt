package com.bookmark.bookmark_oneday.presentation.screens.intro.model

import com.bookmark.bookmark_oneday.R

data class IntroData(
    val idx: Int,
    val titleResourceId: Int,
    val captionResourceId: Int,
    val imageResourceId: Int
) {
    companion object {
        val defaultIntroDataList = listOf(
            IntroData(
                idx = 0,
                titleResourceId = R.string.label_intro_mylibrary,
                captionResourceId = R.string.caption_intro_mylibrary,
                imageResourceId = R.drawable.img_intro_mylibrary
            ),
            IntroData(
                idx = 1,
                titleResourceId = R.string.label_intro_reading_graph,
                captionResourceId = R.string.caption_intro_reading_graph,
                imageResourceId = R.drawable.img_intro_reading_graph
            ),
            IntroData(
                idx = 2,
                titleResourceId = R.string.label_intro_book_group,
                captionResourceId = R.string.caption_intro_book_group,
                imageResourceId = R.drawable.img_intro_book_group
            ),
            IntroData(
                idx = 3,
                titleResourceId = R.string.label_intro_today_one_line,
                captionResourceId = R.string.caption_intro_today_one_line,
                imageResourceId = R.drawable.img_intro_today_one_line
            )
        )
    }
}