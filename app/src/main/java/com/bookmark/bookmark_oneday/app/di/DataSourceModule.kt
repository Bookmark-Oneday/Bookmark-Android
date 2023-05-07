package com.bookmark.bookmark_oneday.app.di

import com.bookmark.bookmark_oneday.data.datasource.application_datasource.ApplicationDataSource
import com.bookmark.bookmark_oneday.data.datasource.application_datasource.ApplicationDataSourceImpl
import com.bookmark.bookmark_oneday.data.datasource.book_datasource.BookDataSource
import com.bookmark.bookmark_oneday.data.datasource.book_datasource.RemoteBookDataSource
import com.bookmark.bookmark_oneday.data.datasource.book_info_datasource.BookInfoDataSource
import com.bookmark.bookmark_oneday.data.datasource.book_info_datasource.KakaoBookInfoDataSource
import com.bookmark.bookmark_oneday.data.datasource.token_datasource.TokenDataSource
import com.bookmark.bookmark_oneday.data.datasource.token_datasource.TokenDataSourceImpl
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
    abstract fun bindTokenDataSource(impl : TokenDataSourceImpl) : TokenDataSource

    @Singleton
    @Binds
    abstract fun bindBookInfoDataSource(impl : KakaoBookInfoDataSource) : BookInfoDataSource

    @Singleton
    @Binds
    abstract fun bindBookDataSource(impl : RemoteBookDataSource) : BookDataSource

    @Singleton
    @Binds
    abstract fun bindApplicationDataSource(impl : ApplicationDataSourceImpl) : ApplicationDataSource
}