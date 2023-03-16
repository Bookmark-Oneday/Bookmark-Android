package com.bookmark.bookmark_oneday.domain.usecase

import kotlinx.coroutines.delay

class UseCaseEditReadingPage {
    suspend operator fun invoke(currentPage : Int, totalPage : Int) : Boolean {
        // 테스트용
        delay(1000L)
        return true
    }
}