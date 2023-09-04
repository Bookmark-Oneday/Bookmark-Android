package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistoryWithBook
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import com.bookmark.bookmark_oneday.domain.book.util.groupByBook
import com.bookmark.bookmark_oneday.domain.book.util.groupByDate
import javax.inject.Inject

class UseCaseGetReadingHistory @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String): BaseResponse<ReadingInfo> {
        return repository.getReadingInfo(bookId)
    }

    suspend fun getHistoryOfMonth(year : Int, month : Int) : BaseResponse<List<ReadingHistory>> {
        val response = repository.getReadingHistoryOfMonth(year, month)

        return if (response is BaseResponse.Success<List<ReadingHistory>>) {
            val data = response.data.groupByDate()
            response.copy(data = data)
        } else {
            response
        }
    }

    suspend fun getHistoryOfDay(year : Int, month : Int, day : Int) : BaseResponse<List<ReadingHistoryWithBook>> {
        val response = repository.getReadingHistoryWithBookOfDay(year, month, day)

        return if (response is BaseResponse.Success<List<ReadingHistoryWithBook>>) {
            val data = response.data.groupByBook()
            response.copy(data = data)
        } else {
            response
        }
    }
}