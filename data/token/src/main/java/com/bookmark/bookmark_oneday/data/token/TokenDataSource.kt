package com.bookmark.bookmark_oneday.data.token

interface TokenDataSource {
    var accessToken : String?
    var refreshToken : String?
}