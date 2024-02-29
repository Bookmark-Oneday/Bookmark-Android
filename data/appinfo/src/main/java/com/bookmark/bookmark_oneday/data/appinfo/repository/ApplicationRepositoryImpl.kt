package com.bookmark.bookmark_oneday.data.appinfo.repository

import androidx.datastore.core.DataStore
import com.bookmark.bookmark_oneday.core.datastore.Alarm
import com.bookmark.bookmark_oneday.data.appinfo.datasource.ApplicationDataSource
import com.bookmark.bookmark_oneday.domain.appinfo.model.AlarmInfo
import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor(
    private val dataSource: ApplicationDataSource,
    private val alarmDataStore : DataStore<Alarm>
) : ApplicationRepository {
    override fun checkFirstExecution(): Boolean {
        return dataSource.getFirstExecution()
    }

    override fun setExecuted() {
        dataSource.setFirstExecution(false)
    }

    override suspend fun setAlarm(alarmOn: Boolean, hour: Int, minute: Int) {
        alarmDataStore.updateData { t: Alarm ->
            t.toBuilder().setAlarmOn(alarmOn).setHour(hour).setMinute(minute).build()
        }
    }

    override fun getAlarmInfo(): Flow<AlarmInfo> = alarmDataStore.data.map {
        AlarmInfo(
            alarmOn = it.alarmOn,
            hour = it.hour,
            minute = it.minute
        )
    }

    override suspend fun setTimerNotificationUsed(useTimerNotification: Boolean) {
        dataSource.setTimerNotificationUsed(useTimerNotification)
    }

    override suspend fun getTimerNotificationUsed(): Boolean {
        return dataSource.getTimerNotificationUsed()
    }
}