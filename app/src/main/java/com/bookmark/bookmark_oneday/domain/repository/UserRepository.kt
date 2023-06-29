package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getGoalReadingTime() : Flow<Int>
    fun getUserProfile() : Flow<UserInfo>
    suspend fun setUserProfile(nickname : String, profileImage : String?, bio : String)
    suspend fun setReadingTime(readingTime : Int)
    suspend fun clearUser()
}