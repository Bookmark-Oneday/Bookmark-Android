package com.bookmark.bookmark_oneday.app.di.data

import com.bookmark.bookmark_oneday.data.appinfo.datasource.ApplicationDataSource
import com.bookmark.bookmark_oneday.data.appinfo.datasource.ApplicationDataSourceImpl
import com.bookmark.bookmark_oneday.data.appinfo.repository.ApplicationRepositoryImpl
import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInfoModule {
    @Singleton
    @Binds
    abstract fun bindApplicationDataSource(impl : ApplicationDataSourceImpl) : ApplicationDataSource

    @Singleton
    @Binds
    abstract fun bindApplicationRepository(impl : ApplicationRepositoryImpl) : ApplicationRepository
}