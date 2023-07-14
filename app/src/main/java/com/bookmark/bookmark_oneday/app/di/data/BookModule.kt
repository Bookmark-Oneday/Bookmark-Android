package com.bookmark.bookmark_oneday.app.di.data

import com.bookmark.bookmark_oneday.core.api.di.KakaoHttpClient
import com.bookmark.bookmark_oneday.core.room.dao.BookDao
import com.bookmark.bookmark_oneday.data.book.datasorce.BookInfoDataSource
import com.bookmark.bookmark_oneday.data.book.datasorce.KakaoBookInfoDataSource
import com.bookmark.bookmark_oneday.data.book.repository.LocalBookRepository
import com.bookmark.bookmark_oneday.data.book.repository.SearchBookDetailRepositoryImpl
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import com.bookmark.bookmark_oneday.domain.book.repository.SearchBookDetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookModule {

    @Singleton
    @Provides
    fun provideLocalBookRepository(
        bookDao: BookDao
    ) : BookRepository {
        return LocalBookRepository(bookDao)
    }

    @Singleton
    @Provides
    fun provideBookInfoDatasource(
        @KakaoHttpClient retrofit : Retrofit
    ) : BookInfoDataSource {
        return KakaoBookInfoDataSource(retrofit)
    }

    @Singleton
    @Provides
    fun provideSearchBookDetailRepository(
        bookInfoDataSource: BookInfoDataSource
    ) : SearchBookDetailRepository {
        return SearchBookDetailRepositoryImpl(bookInfoDataSource)
    }
}