package com.bookmark.bookmark_oneday.app.di

import com.bookmark.bookmark_oneday.data.appinfo.repository.ApplicationRepositoryImpl
import com.bookmark.bookmark_oneday.data.book.repository.SearchBookDetailRepositoryImpl
import com.bookmark.bookmark_oneday.data.oneline.repository.OnelineRepositoryImpl
import com.bookmark.bookmark_oneday.data.repository_impl.*
import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import com.bookmark.bookmark_oneday.domain.book.repository.SearchBookDetailRepository
import com.bookmark.bookmark_oneday.domain.login.repository.GoogleLoginRepository
import com.bookmark.bookmark_oneday.domain.oneline.repository.OnelineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSearchBookDetailRepository(impl : SearchBookDetailRepositoryImpl) : SearchBookDetailRepository

    @Singleton
    @Binds
    abstract fun bindApplicationRepository(impl : ApplicationRepositoryImpl) : ApplicationRepository

    @Singleton
    @Binds
    abstract fun bindGoogleLoginRepository(impl : GoogleLoginRepositoryImpl) : GoogleLoginRepository

    @Singleton
    @Binds
    abstract fun bindOnelineRepository(impl : OnelineRepositoryImpl) : OnelineRepository

}