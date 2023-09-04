package com.bookmark.bookmark_oneday.core.api.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BookmarkOneDayClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleClient
