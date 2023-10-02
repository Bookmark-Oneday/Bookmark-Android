package com.bookmark.bookmark_oneday.data.oneline.model.dto

import com.bookmark.bookmark_oneday.domain.oneline.model.OneLine
import com.bookmark.bookmark_oneday.core.model.toTimeString

data class OneLineDto(
    val id : String,
    val user_id : String,
    val profile_image : String?,
    val nickname : String,
    val book_isbn : String,
    val title : String,
    val authors : List<String>,
    val oneliner : String,
    val color : String,
    val top : String,
    val left : String,
    val font : String,
    val font_size : String,
    val bg_image_url : String?,
    val created_at : String
) {
    companion object {
        fun toOneLine(oneLineDto: OneLineDto) : OneLine {
            val bookInfo = "${oneLineDto.title}, ${oneLineDto.authors.joinToString(",")}"
            val userProfile = com.bookmark.bookmark_oneday.domain.oneline.model.UserProfile(
                id = oneLineDto.user_id,
                profileImageUrl = oneLineDto.profile_image,
                nickname = oneLineDto.nickname
            )

            return OneLine(
                id = oneLineDto.id,
                userProfile = userProfile,
                bookIsbn = oneLineDto.book_isbn,
                bookInfo = bookInfo,
                oneliner = oneLineDto.oneliner,
                textColor = oneLineDto.color,
                centerYPosition = oneLineDto.top.toFloat(),
                centerXPosition = oneLineDto.left.toFloat(),
                fontSize = oneLineDto.font_size.toInt(),
                font = oneLineDto.font,
                backgroundImageUrl = oneLineDto.bg_image_url,
                createdAt = oneLineDto.created_at.toTimeString(),
            )
        }
    }
}
