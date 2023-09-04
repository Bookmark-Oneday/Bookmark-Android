package com.bookmark.bookmark_oneday.app.di.data

import androidx.datastore.core.DataStore
import com.bookmark.bookmark_oneday.core.datastore.User
import com.bookmark.bookmark_oneday.core.room.dao.BookDao
import com.bookmark.bookmark_oneday.core.room.dao.OneLineDao
import com.bookmark.bookmark_oneday.data.oneline.datasource.LocalOneLineDataSource
import com.bookmark.bookmark_oneday.data.oneline.datasource.OnelineDataSource
import com.bookmark.bookmark_oneday.data.oneline.repository.OnelineRepositoryImpl
import com.bookmark.bookmark_oneday.domain.oneline.repository.OnelineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnelineModule {
    @Provides
    @Singleton
    fun provideOnelineDatasource(
        bookDao: BookDao,
        oneLineDao: OneLineDao,
        dataStore: DataStore<User>
    ) : OnelineDataSource {
        return LocalOneLineDataSource(bookDao = bookDao, oneLineDao = oneLineDao, dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideOnelineRepository(
        onelineDataSource: OnelineDataSource
    ) : OnelineRepository {
        return OnelineRepositoryImpl(onelineDataSource)
    }
}