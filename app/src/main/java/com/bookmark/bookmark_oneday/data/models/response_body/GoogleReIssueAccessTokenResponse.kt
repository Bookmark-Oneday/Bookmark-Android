package com.bookmark.bookmark_oneday.data.models.response_body

import com.bookmark.bookmark_oneday.domain.model.TokenInfo

data class GoogleReIssueAccessTokenResponse(
    val access_token : String,
    val expires_in : Int,
    val scope : String,
    val token_type : String,
    val id_token : String?
) {
    companion object {
        fun toTokenInfo(dto : GoogleReIssueAccessTokenResponse) : TokenInfo {
            return TokenInfo(
                accessToken = dto.access_token,
                expiredTime = dto.expires_in,
                refreshToken = null
            )
        }
    }
}
