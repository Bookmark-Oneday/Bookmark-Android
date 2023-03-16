package com.bookmark.bookmark_oneday.domain.usecase

import kotlinx.coroutines.delay

class UseCaseDeleteBook() {
    // 테스트용
    suspend operator fun invoke() : Boolean {
        delay(1000L)
        return true
    }
}