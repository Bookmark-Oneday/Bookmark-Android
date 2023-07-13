package com.bookmark.bookmark_oneday.app.di.data

import com.bookmark.bookmark_oneday.BuildConfig
import com.bookmark.bookmark_oneday.core.api.di.GoogleClient
import com.bookmark.bookmark_oneday.data.google_auth.datasource.GoogleLoginDataSourceImpl
import com.bookmark.bookmark_oneday.data.google_auth.datasource.GoogleLoginDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthModule {
    @Provides
    @Singleton
    fun provideGoogleLoginDataSource(
        @GoogleClient retrofit : Retrofit
    ) : GoogleLoginDataSource {
        return GoogleLoginDataSourceImpl(
            retrofit,
            redirectUri = BuildConfig.GOOGLE_REDIRECT_URI,
            clientId = BuildConfig.GOOGLE_CLIENT_ID,
            clientSecret = BuildConfig.GOOGLE_CLIENT_SECRENT
        )
    }
}