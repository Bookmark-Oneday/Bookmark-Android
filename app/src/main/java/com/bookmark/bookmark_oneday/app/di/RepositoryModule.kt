package com.bookmark.bookmark_oneday.app.di

import com.bookmark.bookmark_oneday.data.repository_impl.*
import com.bookmark.bookmark_oneday.domain.repository.*
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
    abstract fun bindReadingHistoryRepository(impl : ReadingHistoryRepositoryImpl) : ReadingHistoryRepository

    @Singleton
    @Binds
    abstract fun bindMyLibraryRepository(impl : MyLibraryRepositoryImpl) : MyLibraryRepository

    @Singleton
    @Binds
    abstract fun bindRegisterBookRepository(impl : RegisterBookRepositoryImpl) : RegisterBookRepository

    @Singleton
    @Binds
    abstract fun bindSearchBookDetailRepository(impl : SearchBookDetailRepositoryImpl) : SearchBookDetailRepository

    @Singleton
    @Binds
    abstract fun bindSearchBookDeleteRepository(impl : DeleteBookRepositoryImpl) : DeleteBookRepository

    @Singleton
    @Binds
    abstract fun bindBookDetailRepository(impl : BookDetailRepositoryImpl) : BookDetailRepository

    @Singleton
    @Binds
    abstract fun bindEditPageRepository(impl : EditPageRepositoryImpl) : EditPageRepository

    @Singleton
    @Binds
    abstract fun bindApplicationRepository(impl : ApplicationRepositoryImpl) : ApplicationRepository

    @Singleton
    @Binds
    abstract fun bindGoogleLoginRepository(impl : GoogleLoginRepositoryImpl) : GoogleLoginRepository
}