package com.bookmark.bookmark_oneday.data.models.response_body

import com.bookmark.bookmark_oneday.domain.login.model.TokenInfo

data class GoogleAccessTokenResponse(
    val access_token : String,
    val expires_in : Int,
    val scope : String,
    val token_type : String,
    val refresh_token : String,
    val id_token : String?
) {
    companion object {
        fun toTokenInfo(dto : GoogleAccessTokenResponse) : TokenInfo {
            return TokenInfo(
                accessToken = dto.access_token,
                expiredTime = dto.expires_in,
                refreshToken = dto.refresh_token
            )
        }
    }
}