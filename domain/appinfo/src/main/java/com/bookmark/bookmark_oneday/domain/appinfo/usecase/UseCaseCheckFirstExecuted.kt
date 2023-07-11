package com.bookmark.bookmark_oneday.domain.appinfo.usecase

import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import javax.inject.Inject

class UseCaseCheckFirstExecuted @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke() : Boolean {
        return repository.checkFirstExecution()
    }
}