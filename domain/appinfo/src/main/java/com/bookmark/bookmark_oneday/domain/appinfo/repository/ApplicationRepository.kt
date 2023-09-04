package com.bookmark.bookmark_oneday.domain.appinfo.repository

import com.bookmark.bookmark_oneday.domain.appinfo.model.AlarmInfo
import kotlinx.coroutines.flow.Flow

interface ApplicationRepository {
    fun checkFirstExecution() : Boolean
    fun setExecuted()
    suspend fun setAlarm(alarmOn : Boolean, hour : Int = 20, minute : Int = 0)
    fun getAlarmInfo() : Flow<AlarmInfo>
}