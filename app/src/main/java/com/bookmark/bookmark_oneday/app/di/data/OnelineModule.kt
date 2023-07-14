package com.bookmark.bookmark_oneday.app.di.data

import com.bookmark.bookmark_oneday.data.oneline.datasource.OnelineDataSource
import com.bookmark.bookmark_oneday.data.oneline.datasource.TestOnelineDataSource
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
    fun provideOnelineDatasource() : OnelineDataSource {
        return TestOnelineDataSource()
    }

    @Provides
    @Singleton
    fun provideOnelineRepository(
        onelineDataSource: OnelineDataSource
    ) : OnelineRepository {
        return OnelineRepositoryImpl(onelineDataSource)
    }
}