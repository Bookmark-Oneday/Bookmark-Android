package com.bookmark.bookmark_oneday.data.models.dto

import com.bookmark.bookmark_oneday.domain.model.OneLine
import com.bookmark.bookmark_oneday.domain.model.UserProfile
import com.bookmark.bookmark_oneday.domain.model.toTimeString

data class OneLineDto(
    val id : String,
    val user_id : String,
    val profile_image : String?,
    val nickname : String,
    val book_id : String,
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
            val userProfile = UserProfile(id = oneLineDto.user_id, profileImageUrl = oneLineDto.profile_image, nickname = oneLineDto.nickname)

            return OneLine(
                id = oneLineDto.id,
                userProfile = userProfile,
                bookId = oneLineDto.book_id,
                bookInfo = bookInfo,
                oneliner = oneLineDto.oneliner,
                textColor = oneLineDto.color,
                topPosition = oneLineDto.top.toFloat(),
                leftPosition = oneLineDto.left.toFloat(),
                fontSize = oneLineDto.font_size.toInt(),
                backgroundImageUrl = oneLineDto.bg_image_url,
                createdAt = oneLineDto.created_at.toTimeString(),
            )
        }
    }
}
