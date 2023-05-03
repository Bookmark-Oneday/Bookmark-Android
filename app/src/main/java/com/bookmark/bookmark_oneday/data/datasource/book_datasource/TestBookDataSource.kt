package com.bookmark.bookmark_oneday.data.datasource.book_datasource

import com.bookmark.bookmark_oneday.data.models.dto.BookDetailDto
import com.bookmark.bookmark_oneday.data.models.dto.BookItemDto
import com.bookmark.bookmark_oneday.data.models.dto.BookTimerDto
import com.bookmark.bookmark_oneday.data.models.dto.HistoryDto
import com.bookmark.bookmark_oneday.domain.model.*
import kotlinx.coroutines.delay
import javax.inject.Inject

class TestBookDataSource @Inject constructor(

) : BookDataSource {
    override suspend fun registerBook(recognizedBook: RecognizedBook): BaseResponse<Nothing> {
        delay(1000L)
        return BaseResponse.EmptySuccess
    }

    override suspend fun getBookDetail(bookId: String): BaseResponse<BookDetailDto> {
        delay(1000L)
        val testBookDetail = BookDetailDto(
            book_id = bookId,
            title = "세상의 마지막 기차역에서",
            authors = listOf("무라세 다케시"),
            translators = listOf("김지연"),
            publisher = "출판사 이름",
            titleImage = "http://bookthumb.phinf.naver.net/cover/108/346/10834650.jpg?type=m1&udate=20160902",
            current_page = 100,
            total_page = 325,
            history = listOf(
                HistoryDto("1", "2022.12.23T", 3602),
                HistoryDto("2", "2022.12.24T", 1500),
                HistoryDto("3", "2022.12.25T", 3200),
                HistoryDto("4", "2022.12.26T", 4400),
                HistoryDto("5", "2022.12.27T", 3200),
                HistoryDto("6", "2022.12.28T", 2300),
                HistoryDto("7", "2022.12.29T", 0),
                HistoryDto("8", "2022.12.30T", 1500),
                HistoryDto("9", "2022.12.31T", 1200),
            )
        )
        return BaseResponse.Success(testBookDetail)
    }

    override suspend fun getBookList(
        perPage: Int,
        continuousToken: String,
        sortType: String
    ): BaseResponse<PagingData<BookItemDto>> {
        delay(1000L)
        val isFinish = continuousToken.toIntOrNull() == 5 || continuousToken.toIntOrNull() == null
        if (isFinish) {
            return BaseResponse.Success(PagingData(dataList = listOf(), nextPageToken = "", totalItemCount = 150, isFinish = true))
        }

        val nextPageToken = continuousToken.toInt()
        val tempDataList = List(perPage){
            BookItemDto(
                book_id = (it + perPage * nextPageToken).toString(),
                title = "${it + perPage * nextPageToken} 번째 책",
                authors = listOf("${it + perPage * nextPageToken}번째 $sortType"),
                translators = listOf(),
                publisher = "출판사",
                titleImage = "",
                favorite = false,
                reading = false
            )
        }
        val pagingData = PagingData(dataList = tempDataList, nextPageToken = (nextPageToken + 1).toString(), totalItemCount = 150)
        return BaseResponse.Success(pagingData)

    }

    override suspend fun deleteBook(bookId: String): BaseResponse<Nothing> {
        delay(1000L)
        return BaseResponse.EmptySuccess
    }

    override suspend fun updateReadingPage(
        bookId: String,
        currentPage: Int,
        totalPage: Int
    ): BaseResponse<Nothing> {
        delay(1000L)
        return BaseResponse.EmptySuccess
    }

    override suspend fun getBookTimer(bookId: String): BaseResponse<BookTimerDto> {
        delay(1000L)
        val testBookTimerDto = BookTimerDto(
            user_id = "1",
            target_time = 3600,
            daily = 1800,
            book = BookTimerDto.BookTimerBookDto(
                book_id = "1",
                history = listOf(
                    HistoryDto("1", "2022.12.23", 3602),
                    HistoryDto("2", "2022.12.24", 1500),
                    HistoryDto("3", "2022.12.25", 3200),
                    HistoryDto("4", "2022.12.26", 4400),
                    HistoryDto("5", "2022.12.27", 3200),
                    HistoryDto("6", "2022.12.28", 2300),
                    HistoryDto("7", "2022.12.29", 560),
                    HistoryDto("8", "2022.12.30", 1500),
                    HistoryDto("9", "2022.12.31", 1200),
                )
            )
        )
        return BaseResponse.Success(testBookTimerDto)
    }

    override suspend fun updateReadingHistory(
        bookId: String,
        readingTime: Int
    ): BaseResponse<BookTimerDto> {
        delay(1000L)
        val testBookTimerDto = BookTimerDto(
            user_id = "1",
            target_time = 3600,
            daily = 1800,
            book = BookTimerDto.BookTimerBookDto(
                book_id = "1",
                history = listOf(
                    HistoryDto("1", "2022.12.23", 3602),
                    HistoryDto("2", "2022.12.24", 1500),
                    HistoryDto("3", "2022.12.25", 3200),
                    HistoryDto("4", "2022.12.26", 4400),
                    HistoryDto("5", "2022.12.27", 3200),
                    HistoryDto("6", "2022.12.28", 2300),
                    HistoryDto("7", "2022.12.29", 560),
                    HistoryDto("8", "2022.12.30", 1500),
                    HistoryDto("9", "2022.12.31", 1200),
                )
            )
        )
        return BaseResponse.Success(testBookTimerDto)
    }

    override suspend fun deleteReadingHistory(
        bookId: String,
        historyId: String?
    ): BaseResponse<BookTimerDto> {
        delay(1000L)
        val testBookTimerDto = BookTimerDto(
            user_id = "1",
            target_time = 3600,
            daily = 1800,
            book = BookTimerDto.BookTimerBookDto(
                book_id = "1",
                history = if (historyId != null) {
                    listOf(
                        HistoryDto("1", "2022.12.23", 3602),
                        HistoryDto("2", "2022.12.24", 1500),
                        HistoryDto("3", "2022.12.25", 3200),
                        HistoryDto("4", "2022.12.26", 4400),
                        HistoryDto("5", "2022.12.27", 3200),
                        HistoryDto("6", "2022.12.28", 2300),
                        HistoryDto("7", "2022.12.29", 560),
                        HistoryDto("8", "2022.12.30", 1500),
                        HistoryDto("9", "2022.12.31", 1200),
                    ).filter { it.id != historyId }
                } else {
                    listOf()
                }
            )
        )
        return BaseResponse.Success(testBookTimerDto)
    }
}