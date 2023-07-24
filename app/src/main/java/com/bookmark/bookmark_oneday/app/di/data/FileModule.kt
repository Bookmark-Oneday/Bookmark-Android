package com.bookmark.bookmark_oneday.app.di.data

import android.content.Context
import com.bookmark.bookmark_oneday.data.file.repository.LocalFileRepository
import com.bookmark.bookmark_oneday.domain.file.repository.FileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileModule {
    @Provides
    @Singleton
    fun provideFileRepository(
        @ApplicationContext context : Context
    ) : FileRepository {
        return LocalFileRepository(context = context)
    }
}