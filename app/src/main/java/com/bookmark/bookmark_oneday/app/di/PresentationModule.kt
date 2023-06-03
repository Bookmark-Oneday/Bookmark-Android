package com.bookmark.bookmark_oneday.app.di

import android.content.Context
import com.bookmark.bookmark_oneday.presentation.util.DarkModeChecker
import com.bookmark.bookmark_oneday.presentation.util.DarkModeCheckerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {
    @Singleton
    @Provides
    fun bindDarkModeChecker(@ApplicationContext context : Context) : DarkModeChecker {
        return DarkModeCheckerImpl(context)
    }
}
