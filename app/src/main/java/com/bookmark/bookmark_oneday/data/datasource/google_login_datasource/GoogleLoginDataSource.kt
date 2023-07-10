package com.bookmark.bookmark_oneday.data.datasource.google_login_datasource

import com.bookmark.bookmark_oneday.data.models.response_body.GoogleAccessTokenResponse
import com.bookmark.bookmark_oneday.data.models.response_body.GoogleReIssueAccessTokenResponse
import com.bookmark.bookmark_oneday.core.model.BaseResponse

interface GoogleLoginDataSource {
    suspend fun getAccessToken(authCode : String) : BaseResponse<GoogleAccessTokenResponse>
    suspend fun reIssueAccessToken(refreshToken : String) : BaseResponse<GoogleReIssueAccessTokenResponse>
}