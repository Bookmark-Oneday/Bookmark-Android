package com.bookmark.bookmark_oneday.domain.model

data class UserInfo(
    val nickname : String,
    val profileImage : String? = null,
    val bio : String
) {
    companion object {
        val defaultUserInfo = UserInfo(nickname = "loading...", profileImage = null, bio = "로딩중입니다.")
        val emptyUserInfo = UserInfo(nickname = "", profileImage = null, bio = "")
    }
}
