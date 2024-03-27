package com.bookmark.bookmark_oneday.domain.appinfo.usecase

import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import javax.inject.Inject

class UseCaseSetTimerNotificationUsed @Inject constructor(
    private val repository: ApplicationRepository
){
    suspend operator fun invoke(used : Boolean) {
        repository.setTimerNotificationUsed(used)
    }
}