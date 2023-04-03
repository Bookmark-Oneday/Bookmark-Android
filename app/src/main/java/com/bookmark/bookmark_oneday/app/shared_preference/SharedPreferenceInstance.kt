package com.bookmark.bookmark_oneday.app.shared_preference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreferenceInstance {
    private lateinit var sharedPreference : SharedPreferences

    fun init(context : Context) {
        if (!::sharedPreference.isInitialized) {
            sharedPreference = EncryptedSharedPreferences.create(
                context,
                "${context.packageName}_secret_shared_prefs",
                getMasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    private fun getMasterKey(context: Context) =
        MasterKey
            .Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    fun getInstance() = sharedPreference
}