package com.bookmark.bookmark_oneday.data.oneline.datasource

import android.annotation.SuppressLint
import androidx.datastore.core.DataStore
import com.bookmark.bookmark_oneday.core.datastore.User
import com.bookmark.bookmark_oneday.data.oneline.model.dto.OneLineDto
import com.bookmark.bookmark_oneday.data.oneline.model.request.RegisterOneLineRequestBody
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.core.room.dao.BookDao
import com.bookmark.bookmark_oneday.core.room.dao.OneLineDao
import com.bookmark.bookmark_oneday.core.room.entity.OneLineEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocalOneLineDataSource @Inject constructor(
    private val oneLineDao: OneLineDao,
    private val bookDao: BookDao,
    private val dataStore : DataStore<User>
) : OnelineDataSource {
    private val defaultDispatcher: CoroutineContext = Dispatchers.IO + SupervisorJob()
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override suspend fun getOnelineList(
        perPage: Int,
        continuousToken: String,
        sortType: String
    ): BaseResponse<PagingData<OneLineDto>> =
        withContext(defaultDispatcher) {
            try {
                val oneLineList = oneLineDao.getOneLineList(continuousToken.toInt(), perPage)
                val userInfo = dataStore.data.first()

                return@withContext BaseResponse.Success(
                    PagingData(
                        dataList = oneLineList.map {
                           OneLineDto(
                               id = it.id,
                               user_id = "0",
                               profile_image = userInfo.profileUri,
                               nickname = userInfo.nickname,
                               book_isbn = it.isbn,
                               title = it.title,
                               authors = it.authors,
                               oneliner = it.oneliner,
                               color = it.color,
                               top = it.topPosition,
                               left = it.leftPosition,
                               font = it.font,
                               font_size = it.font_size,
                               bg_image_url = it.bg_image_url,
                               created_at = it.created_at
                           )
                        },
                        nextPageToken = continuousToken + 1,
                        totalItemCount = 0
                    )
                )
            } catch (e : Exception) {
                return@withContext BaseResponse.Failure(
                    errorCode = -1,
                    errorMessage = "${e.message}"
                )
            }
        }

    override suspend fun registerOneline(oneLineContent: RegisterOneLineRequestBody): BaseResponse<Nothing> =
        withContext(defaultDispatcher) {
            try {
                val isbn = bookDao.getIsbnByRegisteredBookId(oneLineContent.book_id.toInt())
                val oneLine = OneLineEntity(
                    userId = 0,
                    isbn = isbn,
                    color = oneLineContent.color,
                    centerX = oneLineContent.left,
                    centerY = oneLineContent.top,
                    content = oneLineContent.oneliner,
                    font = oneLineContent.font,
                    fontSize = oneLineContent.font_size,
                    backgroundUri = oneLineContent.bg_image_uri,
                    createdAt =  formatter.format(Calendar.getInstance().time)
                )
                oneLineDao.insertOneLine(oneLine)

                return@withContext BaseResponse.EmptySuccess
            } catch (e : Exception) {
                return@withContext BaseResponse.Failure(
                    errorCode = -1,
                    errorMessage = "${e.message}"
                )
            }
        }

    override suspend fun deleteOneline(id: String): BaseResponse<Nothing> =
        withContext(defaultDispatcher) {
            try {
                oneLineDao.deleteOneline(id)
                return@withContext BaseResponse.EmptySuccess
            } catch (e: Exception) {
                return@withContext BaseResponse.Failure(
                    errorCode = -1,
                    errorMessage = "${e.message}"
                )
            }
        }

}