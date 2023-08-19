package com.bookmark.bookmark_oneday.domain.appinfo.usecase

import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import javax.inject.Inject

class UseCaseSetAlarm @Inject constructor(
    private val repository : ApplicationRepository
) {
    suspend operator fun invoke(alarmOn : Boolean, hour : Int = 20, minute : Int = 0) {
        repository.setAlarm(alarmOn, hour, minute)
    }
}