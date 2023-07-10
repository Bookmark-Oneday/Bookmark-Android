package com.bookmark.bookmark_oneday.app.di.data

import com.bookmark.bookmark_oneday.core.room.dao.BookDao
import com.bookmark.bookmark_oneday.data.book.repository.LocalBookRepository
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookRepositoryModule {

    @Singleton
    @Provides
    fun provideLocalBookRepository(
        bookDao: BookDao
    ) : BookRepository {
        return LocalBookRepository(bookDao)
    }

}