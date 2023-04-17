package com.bookmark.bookmark_oneday.data.datasource.book_datasource

import com.bookmark.bookmark_oneday.app.di.RetrofitModule
import com.bookmark.bookmark_oneday.data.models.dto.BookDetailDto
import com.bookmark.bookmark_oneday.data.models.dto.BookItemDto
import com.bookmark.bookmark_oneday.data.models.dto.BookTimerDto
import com.bookmark.bookmark_oneday.data.models.request_body.RegisterBookRequestBody
import com.bookmark.bookmark_oneday.data.models.request_body.UpdateReadingPageRequestBody
import com.bookmark.bookmark_oneday.data.models.request_body.UpdateReadingTimeRequestBody
import com.bookmark.bookmark_oneday.data.models.response_body.DefaultResponseBody
import com.bookmark.bookmark_oneday.data.models.response_meta.DefaultResponseMeta
import com.bookmark.bookmark_oneday.data.models.response_meta.PagingResponseMeta
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.PagingData
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

// TODO response 받고 실패인 경우 처리하는 코드 중복 extension 으로 해결하기
// TODO 변수명 다 카멜케이스로 변경하기
class RemoteBookDataSource @Inject constructor(
    @RetrofitModule.BookmarkOneDayClient retrofit: Retrofit
) : BookDataSource {

    private val service = retrofit.create(BookDataApi::class.java)

    override suspend fun registerBook(recognizedBook: RecognizedBook): BaseResponse<Nothing> {
        val requestBody = RegisterBookRequestBody.from(recognizedBook)
        val response = service.registerBook(requestBody)

        return if (response.isSuccessful) {
            BaseResponse.EmptySuccess
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun getBookDetail(book_id: String): BaseResponse<BookDetailDto> {
        val response = service.getBookDetail(bookId = book_id)

        return if (response.isSuccessful && response.body() != null) {
            BaseResponse.Success(response.body()!!.data)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun getBookList(
        perPage: Int,
        continuousToken: String,
        sortType: String
    ): BaseResponse<PagingData<BookItemDto>> {
        val response = service.getBookList(sortType, perPage, continuousToken)
        return if (response.isSuccessful && response.body() != null) {
            val pagingData = PagingData(
                dataList = response.body()!!.data,
                nextPageToken = continuousToken + 1,
                totalItemCount = response.body()!!.meta!!.totalCount,
                isFinish = response.body()!!.data.size != perPage,
            )

            BaseResponse.Success(pagingData)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun deleteBook(book_id: String): BaseResponse<Nothing> {
        val response = service.deleteBook(book_id)

        return if (response.isSuccessful) {
            BaseResponse.EmptySuccess
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun updateReadingPage(
        book_id: String,
        current_page: Int,
        total_page: Int
    ): BaseResponse<Nothing> {
        val requestBody = UpdateReadingPageRequestBody(book_id, current_page, total_page)
        val response = service.updateReadingPage(book_id, requestBody)

        return if (response.isSuccessful) {
            BaseResponse.EmptySuccess
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun getBookTimer(book_id: String): BaseResponse<BookTimerDto> {
        val response = service.getBookTimer(book_id)

        return if (response.isSuccessful && response.body() != null) {
            BaseResponse.Success(response.body()!!.data)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun updateReadingHistory(
        book_id: String,
        reading_time: Int
    ): BaseResponse<BookTimerDto> {
        val requestBody = UpdateReadingTimeRequestBody(book_id, reading_time)
        val response = service.updateReadingTime(requestBody)

        return if (response.isSuccessful && response.body() != null) {
            BaseResponse.Success(response.body()!!.data)
        }  else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    override suspend fun deleteReadingHistory(
        book_id: String,
        historyId: String?
    ): BaseResponse<BookTimerDto> {
        val response = service.deleteReadingHistory(bookId = book_id, historyId = historyId)

        return if (response.isSuccessful && response.body() != null) {
            BaseResponse.Success(response.body()!!.data)
        }  else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }
}

interface BookDataApi {
    @GET("/v1/library/mylist")
    suspend fun getBookList(@Query("sortType") sortType : String, @Query("perPage") perPage : Int, @Query("continuousToken") continuousToken : String) : Response<DefaultResponseBody<List<BookItemDto>, PagingResponseMeta>>

    @GET("/v1/library/mylist/{bookId}")
    suspend fun getBookDetail(@Path("bookId") bookId : String) : Response<DefaultResponseBody<BookDetailDto, DefaultResponseMeta>>

    @POST("/v1/library/mylist/book")
    suspend fun registerBook(@Body params : RegisterBookRequestBody) : Response<ResponseBody>

    @DELETE("/v1/library/timer/{bookId}")
    suspend fun deleteBook(@Path("bookId") bookId : String) : Response<ResponseBody>

    @POST("/v1/library/lastpage/{bookId}")
    suspend fun updateReadingPage(@Path("bookId") bookId : String, @Body params : UpdateReadingPageRequestBody) : Response<ResponseBody>

    @GET("/v1/library/timer/{bookId}")
    suspend fun getBookTimer(@Path("bookId") bookId : String) : Response<DefaultResponseBody<BookTimerDto, DefaultResponseMeta>>

    @POST("/v1/library/timer/{bookId}")
    suspend fun updateReadingTime(@Body params : UpdateReadingTimeRequestBody) : Response<DefaultResponseBody<BookTimerDto, DefaultResponseMeta>>

    @DELETE("/v1/library/timer/{bookId}")
    suspend fun deleteReadingHistory(@Path("bookId") bookId : String, @Query("historyId") historyId : String?) : Response<DefaultResponseBody<BookTimerDto, DefaultResponseMeta>>
}