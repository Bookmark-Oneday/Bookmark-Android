package com.bookmark.bookmark_oneday.app.di

import com.bookmark.bookmark_oneday.data.appinfo.datasource.ApplicationDataSource
import com.bookmark.bookmark_oneday.data.appinfo.datasource.ApplicationDataSourceImpl
import com.bookmark.bookmark_oneday.data.book.datasorce.BookInfoDataSource
import com.bookmark.bookmark_oneday.data.book.datasorce.KakaoBookInfoDataSource
import com.bookmark.bookmark_oneday.data.oneline.datasource.OnelineDataSource
import com.bookmark.bookmark_oneday.data.oneline.datasource.TestOnelineDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindBookInfoDataSource(impl : KakaoBookInfoDataSource) : BookInfoDataSource

    @Singleton
    @Binds
    abstract fun bindApplicationDataSource(impl : ApplicationDataSourceImpl) : ApplicationDataSource

    @Singleton
    @Binds
    abstract fun bindOnelineDataSource(impl : TestOnelineDataSource) : OnelineDataSource
}