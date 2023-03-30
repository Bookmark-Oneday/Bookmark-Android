package com.bookmark.bookmark_oneday.app.di

import com.bookmark.bookmark_oneday.app.retrofit.KakaoRetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoHttpClient

    @KakaoHttpClient
    @Provides
    fun provideKakaoHttpRetrofit() : Retrofit {
        KakaoRetrofitInstance.init()
        return KakaoRetrofitInstance.getInstance()
    }
}