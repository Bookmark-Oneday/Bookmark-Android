package com.bookmark.bookmark_oneday.app.di.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.bookmark.bookmark_oneday.core.datastore.Alarm
import com.bookmark.bookmark_oneday.core.datastore.AlarmDataStoreSerializer
import com.bookmark.bookmark_oneday.core.datastore.User
import com.bookmark.bookmark_oneday.core.datastore.UserDataStoreSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "oneday_user.pb"
private const val DATA_STORE_ALARM_FILE_NAME = "oneday_alarm.pb"

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {
    @Singleton
    @Provides
    fun provideProtoDataStore(@ApplicationContext appContext : Context) : DataStore<User> {
        return DataStoreFactory.create(
            serializer = UserDataStoreSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideAlarmProtoDataStore(@ApplicationContext appContext : Context) : DataStore<Alarm> {
        return DataStoreFactory.create(
            serializer = AlarmDataStoreSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_ALARM_FILE_NAME) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}