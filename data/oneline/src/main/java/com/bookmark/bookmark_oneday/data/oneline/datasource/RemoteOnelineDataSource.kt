package com.bookmark.bookmark_oneday.data.oneline.datasource

import com.bookmark.bookmark_oneday.core.api.di.BookmarkOneDayClient
import com.bookmark.bookmark_oneday.data.oneline.model.dto.OneLineDto
import com.bookmark.bookmark_oneday.data.oneline.model.request.RegisterOneLineRequestBody
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.data.oneline.model.response.DefaultResponseBody
import com.bookmark.bookmark_oneday.data.oneline.model.response.PagingResponseMeta
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject

class RemoteOnelineDataSource @Inject constructor(
    @BookmarkOneDayClient retrofit : Retrofit
) : OnelineDataSource {
    private val service = retrofit.create(OnelineApi::class.java)

    override suspend fun getOnelineList(
        perPage: Int,
        continuousToken: String,
        sortType: String
    ): BaseResponse<PagingData<OneLineDto>> {
        val response = service.getOnelineList(sortType, perPage, continuousToken)
        return if (response.isSuccessful && response.body() != null) {
            val pagingData = PagingData(
                dataList = response.body()!!.data,
                nextPageToken = continuousToken + 1,
                totalItemCount = response.body()!!.meta!!.totalCount,
                isFinish = response.body()!!.data.size != perPage
            )
            BaseResponse.Success(pagingData)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun registerOneline(oneLineContent: RegisterOneLineRequestBody): BaseResponse<Nothing> {
        val response = service.registerOneline(oneLineContent)
        return if (response.isSuccessful) {
            BaseResponse.EmptySuccess
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun deleteOneline(id: String): BaseResponse<Nothing> {
        val response = service.deleteOneline(id)
        return if (response.isSuccessful) {
            BaseResponse.EmptySuccess
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }
}

interface OnelineApi {
    @GET("/v1/oneliner")
    suspend fun getOnelineList(@Query("sortType") sortType : String, @Query("perPage") perPage : Int, @Query("continuousToken") continuousToken : String) : Response<DefaultResponseBody<List<OneLineDto>, PagingResponseMeta>>

    @POST("/v1/oneliner")
    suspend fun registerOneline(@Body params : RegisterOneLineRequestBody) : Response<ResponseBody>

    @DELETE("/v1/oneliner")
    suspend fun deleteOneline(@Query("id") id : String) : Response<ResponseBody>
}