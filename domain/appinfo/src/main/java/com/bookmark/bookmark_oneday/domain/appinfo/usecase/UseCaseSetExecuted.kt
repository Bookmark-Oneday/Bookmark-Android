package com.bookmark.bookmark_oneday.domain.appinfo.usecase

import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import javax.inject.Inject

class UseCaseSetExecuted @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke() {
        repository.setExecuted()
    }
}