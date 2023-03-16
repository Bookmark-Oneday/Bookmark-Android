package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.model.BookDetail
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import kotlinx.coroutines.delay

class UseCaseGetBookDetail() {
    // todo 테스트용 코드 수정하기
    suspend fun invoke(bookId : Int) : BookDetail {
        delay(1000L)

        return BookDetail(
            bookId = bookId,
            title = "세상의 마지막 기차역에서",
            author = "무라세 다케시",
            translators = "김지연",
            publisher = "",
            imageUrl = "http://bookthumb.phinf.naver.net/cover/108/346/10834650.jpg?type=m1&udate=20160902",
            currentPage = 100,
            totalPage = 325,
            history = listOf(
                ReadingHistory(1, "2022.12.23", 3602),
                ReadingHistory(2, "2022.12.24", 1500),
                ReadingHistory(3, "2022.12.25", 3200),
                ReadingHistory(4, "2022.12.26", 4400),
                ReadingHistory(5, "2022.12.27", 3200),
                ReadingHistory(6, "2022.12.28", 2300),
                ReadingHistory(7, "2022.12.29", 560),
                ReadingHistory(8, "2022.12.30", 1500),
                ReadingHistory(9, "2022.12.31", 1200),
            )
        )
    }
}