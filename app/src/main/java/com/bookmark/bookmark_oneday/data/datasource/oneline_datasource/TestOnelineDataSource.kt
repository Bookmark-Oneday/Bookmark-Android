package com.bookmark.bookmark_oneday.data.datasource.oneline_datasource

import com.bookmark.bookmark_oneday.data.models.dto.OneLineDto
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.PagingData
import kotlinx.coroutines.delay
import javax.inject.Inject

class TestOnelineDataSource @Inject constructor() : OnelineDataSource {
    override suspend fun getOnelineList(
        perPage: Int,
        continuousToken: String,
        sortType: String
    ): BaseResponse<PagingData<OneLineDto>> {
        delay(1000L)
        val isFinish = continuousToken.toIntOrNull() == 5 || continuousToken.toIntOrNull() == null
        if (isFinish) {
            return BaseResponse.Success(PagingData(dataList = listOf(), nextPageToken = "", totalItemCount = 150, isFinish = true))
        }

        val nextPageToken = continuousToken.toInt()
        val tempDataList = List(perPage){
            OneLineDto(
                id = "$continuousToken-$it",
                user_id = "1",
                profile_image = null,
                nickname = "Test User $continuousToken-$it",
                book_id = "1",
                title = "Test $continuousToken $it",
                authors = listOf("Oauth", "2.0"),
                oneliner = "$continuousToken $it 테스트 중입니다~",
                color = if (it == 0) "#FF018786" else  "#000000",
                top = if (it % 2 == 0) "0.9" else "0",
                left = if (it % 3 == 0) "0.9" else "0",
                font = "Test",
                font_size = (18 + it * 3).toString(),
                bg_image_url = null,
                created_at = "2023-04-29T11:18:25.801Z"
            )
        }
        val pagingData = PagingData(dataList = tempDataList, nextPageToken = (nextPageToken + 1).toString(), totalItemCount = 150)
        return BaseResponse.Success(pagingData)
    }
}