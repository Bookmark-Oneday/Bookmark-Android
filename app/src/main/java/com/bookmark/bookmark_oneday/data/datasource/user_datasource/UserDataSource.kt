package com.bookmark.bookmark_oneday.data.datasource.user_datasource

import com.bookmark.bookmark_oneday.data.models.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getUser() : Flow<UserEntity>
    suspend fun setUser(nickname : String, profileUri : String?, bio : String)
    suspend fun setReadTime(readTime : Int)
}