package com.bookmark.bookmark_oneday.domain.model

data class TokenInfo(val accessToken : String, val refreshToken : String?, val expiredTime : Int)