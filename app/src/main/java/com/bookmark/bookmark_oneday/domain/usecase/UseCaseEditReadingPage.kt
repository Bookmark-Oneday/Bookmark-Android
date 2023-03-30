package com.bookmark.bookmark_oneday.domain.usecase

import kotlinx.coroutines.delay
import javax.inject.Inject

class UseCaseEditReadingPage @Inject constructor(

) {
    suspend operator fun invoke(currentPage : Int, totalPage : Int) : Boolean {
        // 테스트용
        delay(1000L)
        return true
    }
}