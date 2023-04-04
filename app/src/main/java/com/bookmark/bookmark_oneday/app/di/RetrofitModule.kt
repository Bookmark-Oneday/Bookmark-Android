package com.bookmark.bookmark_oneday.app.di

import com.bookmark.bookmark_oneday.app.retrofit.KakaoRetrofitInstance
import com.bookmark.bookmark_oneday.app.retrofit.BookMarkOneDayRetrofit
import com.bookmark.bookmark_oneday.data.datasource.token_datasource.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BookmarkOneDayClient

    @KakaoHttpClient
    @Singleton
    @Provides
    fun provideKakaoHttpRetrofit() : Retrofit {
        KakaoRetrofitInstance.init()
        return KakaoRetrofitInstance.getInstance()
    }

    @BookmarkOneDayClient
    @Singleton
    @Provides
    fun provideBookmarkOneDayRetrofit(tokenDataSource: TokenDataSource) : Retrofit {
        val retrofit = BookMarkOneDayRetrofit(tokenDataSource)
        retrofit.init()
        return retrofit.getRetrofit()
    }
}