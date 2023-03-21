package com.bookmark.bookmark_oneday.app

import android.app.Application
import com.bookmark.bookmark_oneday.app.retrofit.KakaoRetrofitInstance
import com.bookmark.bookmark_oneday.app.retrofit.RetrofitInstance
import com.bookmark.bookmark_oneday.app.shared_preference.SharedPreferenceInstance
import com.bookmark.bookmark_oneday.data.datasource.token_datasource.TokenDataSourceImpl

class DefaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SharedPreferenceInstance.init(this)
        KakaoRetrofitInstance.init()
        RetrofitInstance.init(tokenDataSource = TokenDataSourceImpl(SharedPreferenceInstance.getInstance()))
    }
}