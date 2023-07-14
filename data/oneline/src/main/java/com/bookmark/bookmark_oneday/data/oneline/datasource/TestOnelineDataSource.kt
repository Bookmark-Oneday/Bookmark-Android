package com.bookmark.bookmark_oneday.data.oneline.datasource

import com.bookmark.bookmark_oneday.data.oneline.model.dto.OneLineDto
import com.bookmark.bookmark_oneday.data.oneline.model.request.RegisterOneLineRequestBody
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData
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
//        val tempDataList = List(perPage){
//            OneLineDto(
//                id = "$continuousToken-$it",
//                user_id = "1",
//                profile_image = null,
//                nickname = "Test User $continuousToken-$it",
//                book_id = "1",
//                title = "Test $continuousToken $it",
//                authors = listOf("Oauth", "2.0"),
//                oneliner = "$continuousToken $it 테스트 중입니다~",
//                color = if (it == 0) "#FF018786" else  "#000000",
//                top = if (it % 2 == 0) "0.9" else "0",
//                left = if (it % 3 == 0) "0.9" else "0",
//                font = "Test",
//                font_size = (18 + it * 3).toString(),
//                bg_image_url = null,
//                created_at = "2023-04-29T11:18:25.801Z"
//            )
//        }

        val startIndex = perPage * nextPageToken
        val endIndex = perPage * (nextPageToken + 1)

        val tempDataList = if (startIndex >= testOnelineList.size) {
            emptyList()
        } else {
            testOnelineList.subList(
                startIndex.coerceAtLeast(0), endIndex.coerceAtMost(testOnelineList.size)
            )
        }

        val pagingData = PagingData(dataList = tempDataList, nextPageToken = (nextPageToken + 1).toString(), totalItemCount = 150, isFinish = tempDataList.isEmpty())
        return BaseResponse.Success(pagingData)
    }

    private val testOnelineList = listOf(
        OneLineDto(
            id = "1", user_id = "1", profile_image = null,
            nickname = "플레이스", book_id = "1",
            title = "차라투스트라는 이렇게 말했다", authors = listOf("프리드리히 니체"),
            oneliner = "사랑에는 늘 약간의 망상이 들어 있다.", color = "#ffffff",
            top = "0.5", left = "0.5", font = "Test", font_size = "18",
            bg_image_url = "https://picsum.photos/id/56/300/720",
            created_at = "2023-04-29T11:18:25.801Z"
        ),
        OneLineDto(
            id = "2", user_id = "1", profile_image = null,
            nickname = "더파인애플", book_id = "1",
            title = "여행의 이유", authors = listOf("김영하"),
            oneliner = "여행가고싶다", color = "#ffffff",
            top = "0.0", left = "0.0", font = "Test", font_size = "24",
            bg_image_url = "https://picsum.photos/id/120/360/720",
            created_at = "2023-04-29T11:18:25.801Z"
        ),
        OneLineDto(
            id = "3", user_id = "1", profile_image = null,
            nickname = "프로브", book_id = "1",
            title = "Clean Code(클린 코드)", authors = listOf("로버트 C. 마틴"),
            oneliner = "clean code, clean state", color = "#000000",
            top = "0.40", left = "0.08", font = "Test", font_size = "30",
            bg_image_url = "https://picsum.photos/id/36/280/720",
            created_at = "2023-04-29T11:18:25.801Z"
        ),
        OneLineDto(
            id = "4", user_id = "1", profile_image = null,
            nickname = "플레이스", book_id = "1",
            title = "차라투스트라는 이렇게 말했다", authors = listOf("프리드리히 니체"),
            oneliner = "사랑에는 늘 약간의 망상이 들어 있다.", color = "#ffffff",
            top = "0.5", left = "0.5", font = "Test", font_size = "18",
            bg_image_url = "https://picsum.photos/id/56/300/720",
            created_at = "2023-04-29T11:18:25.801Z"
        ),
        OneLineDto(
            id = "5", user_id = "1", profile_image = null,
            nickname = "더파인애플", book_id = "1",
            title = "여행의 이유", authors = listOf("김영하"),
            oneliner = "여행가고싶다", color = "#ffffff",
            top = "0.0", left = "0.0", font = "Test", font_size = "24",
            bg_image_url = "https://picsum.photos/id/120/360/720",
            created_at = "2023-04-29T11:18:25.801Z"
        ),
        OneLineDto(
            id = "6", user_id = "1", profile_image = null,
            nickname = "프로브", book_id = "1",
            title = "Clean Code(클린 코드)", authors = listOf("로버트 C. 마틴"),
            oneliner = "clean code, clean state", color = "#000000",
            top = "0.40", left = "0.08", font = "Test", font_size = "30",
            bg_image_url = "https://picsum.photos/id/36/280/720",
            created_at = "2023-04-29T11:18:25.801Z"
        ),
    )

    override suspend fun registerOneline(oneLineContent: RegisterOneLineRequestBody): BaseResponse<Nothing> {
        delay(1500L)
        return BaseResponse.EmptySuccess
    }
}