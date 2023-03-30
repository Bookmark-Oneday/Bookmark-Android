package com.bookmark.bookmark_oneday.app.di

import com.bookmark.bookmark_oneday.data.repository_impl.MyLibraryRepositoryImpl
import com.bookmark.bookmark_oneday.data.repository_impl.ReadingHistoryRepositoryImpl
import com.bookmark.bookmark_oneday.data.repository_impl.RegisterBookRepositoryImpl
import com.bookmark.bookmark_oneday.data.repository_impl.SearchBookDetailRepositoryImpl
import com.bookmark.bookmark_oneday.domain.repository.MyLibraryRepository
import com.bookmark.bookmark_oneday.domain.repository.ReadingHistoryRepository
import com.bookmark.bookmark_oneday.domain.repository.RegisterBookRepository
import com.bookmark.bookmark_oneday.domain.repository.SearchBookDetailRepository
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
    abstract fun provideReadingHistoryRepository(impl : ReadingHistoryRepositoryImpl) : ReadingHistoryRepository

    @Singleton
    @Binds
    abstract fun provideMyLibraryRepository(impl : MyLibraryRepositoryImpl) : MyLibraryRepository

    @Singleton
    @Binds
    abstract fun provideRegisterBookRepository(impl : RegisterBookRepositoryImpl) : RegisterBookRepository

    @Singleton
    @Binds
    abstract fun provideSearchBookDetailRepository(impl : SearchBookDetailRepositoryImpl) : SearchBookDetailRepository
}