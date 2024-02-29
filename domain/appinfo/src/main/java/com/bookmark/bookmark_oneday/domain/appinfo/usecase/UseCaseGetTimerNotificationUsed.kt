package com.bookmark.bookmark_oneday.domain.appinfo.usecase

import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import javax.inject.Inject

class UseCaseGetTimerNotificationUsed @Inject constructor(
    private val repository: ApplicationRepository
) {
    suspend operator fun invoke() : Boolean {
        return repository.getTimerNotificationUsed()
    }
}