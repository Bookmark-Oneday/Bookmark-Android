package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.repository.ApplicationRepository
import javax.inject.Inject

class UseCaseCheckFirstExecuted @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke() : Boolean {
        return repository.checkFirstExecution()
    }
}