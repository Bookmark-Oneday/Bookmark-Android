package com.bookmark.bookmark_oneday.data.google_auth.datasource

import com.bookmark.bookmark_oneday.core.api.di.GoogleClient
import com.bookmark.bookmark_oneday.data.google_auth.model.response.GoogleAccessTokenResponse
import com.bookmark.bookmark_oneday.data.google_auth.model.response.GoogleReIssueAccessTokenResponse
import com.bookmark.bookmark_oneday.data.google_auth.model.request.RequestIssueGoogleAccessToken
import com.bookmark.bookmark_oneday.data.google_auth.model.request.RequestReIssueGoogleAccessToken
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class GoogleLoginDataSourceImpl @Inject constructor(
    @GoogleClient retrofit : Retrofit,
    private val redirectUri : String,
    private val clientId : String,
    private val clientSecret : String
) : GoogleLoginDataSource {
    private val service = retrofit.create(GoogleLoginApi::class.java)

    override suspend fun getAccessToken(authCode: String): BaseResponse<GoogleAccessTokenResponse> {
        val requestBody = RequestIssueGoogleAccessToken(
            code = authCode,
            redirect_uri = redirectUri,
            client_id = clientId,
            client_secret = clientSecret
        )

        val response = service.getAccessToken(requestBody)

        return if (response.isSuccessful && response.body() != null) {
            BaseResponse.Success(response.body()!!)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun reIssueAccessToken(refreshToken: String): BaseResponse<GoogleReIssueAccessTokenResponse> {
        val requestBody = RequestReIssueGoogleAccessToken(
            refresh_token = refreshToken,
            client_id = clientId,
            client_secret = clientSecret
        )

        val response = service.reIssueAccessToken(requestBody)

        return if (response.isSuccessful && response.body() != null) {
            BaseResponse.Success(response.body()!!)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }
}

interface GoogleLoginApi {
    @POST("/token")
    suspend fun getAccessToken(@Body params : RequestIssueGoogleAccessToken) : Response<GoogleAccessTokenResponse>

    @POST("/token")
    suspend fun reIssueAccessToken(@Body params : RequestReIssueGoogleAccessToken) : Response<GoogleReIssueAccessTokenResponse>
}