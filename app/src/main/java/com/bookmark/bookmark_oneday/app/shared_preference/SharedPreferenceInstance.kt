package com.bookmark.bookmark_oneday.app.shared_preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreferenceInstance {
    private lateinit var sharedPreference : SharedPreferences

    fun init(context : Context) {
        if (!::sharedPreference.isInitialized) {
            sharedPreference = context.getSharedPreferences("book_mark_one_day_secret_shared_prefs", MODE_PRIVATE)
        }
    }

    fun getInstance() = sharedPreference
}