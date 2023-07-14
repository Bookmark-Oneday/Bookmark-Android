package com.bookmark.bookmark_oneday.app.di.core

import com.bookmark.bookmark_oneday.BuildConfig
import com.bookmark.bookmark_oneday.core.api.kakao.KakaoRetrofitInstance
import com.bookmark.bookmark_oneday.core.api.bookmark.BookMarkOneDayRetrofit
import com.bookmark.bookmark_oneday.core.api.di.BookmarkOneDayClient
import com.bookmark.bookmark_oneday.core.api.di.GoogleClient
import com.bookmark.bookmark_oneday.core.api.di.KakaoHttpClient
import com.bookmark.bookmark_oneday.core.api.google.GoogleLoginRetrofitInstance
import com.bookmark.bookmark_oneday.data.token.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @KakaoHttpClient
    @Singleton
    @Provides
    fun provideKakaoHttpRetrofit() : Retrofit {
        KakaoRetrofitInstance.init(BuildConfig.KAKAO_KEY)
        return KakaoRetrofitInstance.getInstance()
    }

    @BookmarkOneDayClient
    @Singleton
    @Provides
    fun provideBookmarkOneDayRetrofit(tokenDataSource: TokenDataSource) : Retrofit {
        val retrofit = BookMarkOneDayRetrofit(tokenDataSource::accessToken, tokenDataSource::refreshToken)
        retrofit.init()
        return retrofit.getRetrofit()
    }

    @GoogleClient
    @Singleton
    @Provides
    fun provideGoogleRetrofit() : Retrofit {
        GoogleLoginRetrofitInstance.init()
        return GoogleLoginRetrofitInstance.getInstance()
    }
}