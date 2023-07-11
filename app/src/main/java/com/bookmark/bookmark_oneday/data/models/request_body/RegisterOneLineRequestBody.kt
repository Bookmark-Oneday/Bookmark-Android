package com.bookmark.bookmark_oneday.data.models.request_body

import com.bookmark.bookmark_oneday.domain.oneline.model.OneLineContent

data class RegisterOneLineRequestBody(
    val book_id : String,
    val title : String,
    val authors : List<String>,
    val oneliner : String,
    val color : String,
    val top : String,
    val left : String,
    val font : String,
    val font_size : String,
    val bg_image_uri : String?
) {
    companion object {
        fun fromOnelineContent(oneLineContent: OneLineContent) : RegisterOneLineRequestBody {

            return RegisterOneLineRequestBody(
                book_id = oneLineContent.id,
                title = oneLineContent.bookTitle,
                authors = oneLineContent.authors,
                oneliner = oneLineContent.oneliner,
                color = oneLineContent.textColor,
                top = oneLineContent.centerY,
                left = oneLineContent.centerX,
                font = oneLineContent.font,
                font_size = oneLineContent.textSize,
                bg_image_uri = oneLineContent.backgroundImageUri
            )
        }
    }
}