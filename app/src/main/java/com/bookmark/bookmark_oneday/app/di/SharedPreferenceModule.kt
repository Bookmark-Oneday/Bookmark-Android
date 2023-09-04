package com.bookmark.bookmark_oneday.app.di

import android.content.Context
import android.content.SharedPreferences
import com.bookmark.bookmark_oneday.app.shared_preference.SharedPreferenceInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {
    @Provides
    fun provideSharedPreference(@ApplicationContext context : Context) : SharedPreferences {
        SharedPreferenceInstance.init(context)
        return SharedPreferenceInstance.getInstance()
    }
}