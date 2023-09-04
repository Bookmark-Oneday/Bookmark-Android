package com.bookmark.bookmark_oneday.domain.login.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.login.model.TokenInfo

interface GoogleLoginRepository {
    suspend fun getAccessToken(authCode : String) : BaseResponse<TokenInfo>
    suspend fun reIssueAccessToken(refreshToken : String) : BaseResponse<TokenInfo>
}