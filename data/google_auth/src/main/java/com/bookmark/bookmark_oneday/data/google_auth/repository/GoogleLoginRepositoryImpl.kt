package com.bookmark.bookmark_oneday.data.google_auth.repository

import com.bookmark.bookmark_oneday.data.google_auth.datasource.GoogleLoginDataSource
import com.bookmark.bookmark_oneday.data.google_auth.model.response.GoogleAccessTokenResponse
import com.bookmark.bookmark_oneday.data.google_auth.model.response.GoogleReIssueAccessTokenResponse
import com.bookmark.bookmark_oneday.data.google_auth.util.mapBaseResponse
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.login.model.TokenInfo
import com.bookmark.bookmark_oneday.domain.login.repository.GoogleLoginRepository
import javax.inject.Inject

class GoogleLoginRepositoryImpl @Inject constructor(
    private val googleLoginDataSource : GoogleLoginDataSource
) : GoogleLoginRepository {
    override suspend fun getAccessToken(authCode: String): BaseResponse<TokenInfo> {
        val response = googleLoginDataSource.getAccessToken(authCode)
        return mapBaseResponse(response, GoogleAccessTokenResponse::toTokenInfo)
    }

    override suspend fun reIssueAccessToken(refreshToken: String): BaseResponse<TokenInfo> {
        val response = googleLoginDataSource.reIssueAccessToken(refreshToken)
        return mapBaseResponse(response, GoogleReIssueAccessTokenResponse::toTokenInfo)
    }
}