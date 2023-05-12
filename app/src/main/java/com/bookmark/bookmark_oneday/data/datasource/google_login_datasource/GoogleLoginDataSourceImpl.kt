package com.bookmark.bookmark_oneday.data.datasource.google_login_datasource

import com.bookmark.bookmark_oneday.BuildConfig
import com.bookmark.bookmark_oneday.app.di.RetrofitModule
import com.bookmark.bookmark_oneday.data.models.response_body.GoogleAccessTokenResponse
import com.bookmark.bookmark_oneday.data.models.response_body.GoogleReIssueAccessTokenResponse
import com.bookmark.bookmark_oneday.data.models.request_body.RequestIssueGoogleAccessToken
import com.bookmark.bookmark_oneday.data.models.request_body.RequestReIssueGoogleAccessToken
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class GoogleLoginDataSourceImpl @Inject constructor(
    @RetrofitModule.GoogleClient retrofit : Retrofit
) : GoogleLoginDataSource {
    private val service = retrofit.create(GoogleLoginApi::class.java)

    override suspend fun getAccessToken(authCode: String): BaseResponse<GoogleAccessTokenResponse> {
        val requestBody = RequestIssueGoogleAccessToken(
            code = authCode,
            redirect_uri = BuildConfig.GOOGLE_REDIRECT_URI,
            client_id = BuildConfig.GOOGLE_CLIENT_ID,
            client_secret = BuildConfig.GOOGLE_CLIENT_SECRENT
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
            client_id = BuildConfig.GOOGLE_CLIENT_ID,
            client_secret = BuildConfig.GOOGLE_CLIENT_SECRENT,
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