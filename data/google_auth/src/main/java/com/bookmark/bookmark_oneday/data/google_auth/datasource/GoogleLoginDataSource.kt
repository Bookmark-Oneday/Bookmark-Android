package com.bookmark.bookmark_oneday.data.google_auth.datasource

import com.bookmark.bookmark_oneday.data.google_auth.model.response.GoogleAccessTokenResponse
import com.bookmark.bookmark_oneday.data.google_auth.model.response.GoogleReIssueAccessTokenResponse
import com.bookmark.bookmark_oneday.core.model.BaseResponse

interface GoogleLoginDataSource {
    suspend fun getAccessToken(authCode : String) : BaseResponse<GoogleAccessTokenResponse>
    suspend fun reIssueAccessToken(refreshToken : String) : BaseResponse<GoogleReIssueAccessTokenResponse>
}