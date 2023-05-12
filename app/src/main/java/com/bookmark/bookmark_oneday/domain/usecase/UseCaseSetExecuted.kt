package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.repository.ApplicationRepository
import javax.inject.Inject

class UseCaseSetExecuted @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke() {
        repository.setExecuted()
    }
}