package com.bookmark.bookmark_oneday.domain.login.model

data class TokenInfo(val accessToken : String, val refreshToken : String?, val expiredTime : Int)