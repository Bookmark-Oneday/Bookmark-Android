package com.bookmark.bookmark_oneday.domain.oneline.model

// todo 이름을 OneLineUserProfile 이런 식으로 변경하기
// 이후 UserProfile 네이밍은 UserInfo 로 옮길 것
data class UserProfile (
    val id : String,
    val profileImageUrl : String ?= null,
    val nickname : String
)