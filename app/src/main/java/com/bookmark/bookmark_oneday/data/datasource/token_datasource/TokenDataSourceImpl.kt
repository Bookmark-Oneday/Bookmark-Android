package com.bookmark.bookmark_oneday.data.datasource.token_datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class TokenDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenDataSource {
    override var accessToken: String?
        get() = sharedPreferences.getString(ACCESS_TOKEN, "74d18bfc-14c5-46d2-a1a8-1eb627918859")
        set(value) { sharedPreferences.edit { putString(ACCESS_TOKEN, value) } }

    override var refreshToken: String?
        get() = sharedPreferences.getString(REFRESH_TOKEN, null)
        set(value) { sharedPreferences.edit { putString(REFRESH_TOKEN, value) } }

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}