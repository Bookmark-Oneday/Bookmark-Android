package com.bookmark.bookmark_oneday.app.di

import android.content.Context
import com.bookmark.bookmark_oneday.data.room_database.database.BookmarkRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    @Provides
    @Singleton
    fun provideBookmarkRoomDatabase(@ApplicationContext context : Context) : BookmarkRoomDatabase {
        return BookmarkRoomDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideBookDao(database : BookmarkRoomDatabase) = database.bookDao()

    @Provides
    @Singleton
    fun provideOneLineDao(database: BookmarkRoomDatabase) = database.oneLineDao()
}