package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.google_login_datasource.GoogleLoginDataSource
import com.bookmark.bookmark_oneday.data.models.response_body.GoogleAccessTokenResponse
import com.bookmark.bookmark_oneday.data.models.response_body.GoogleReIssueAccessTokenResponse
import com.bookmark.bookmark_oneday.data.utils.mapBaseResponse
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.TokenInfo
import com.bookmark.bookmark_oneday.domain.repository.GoogleLoginRepository
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