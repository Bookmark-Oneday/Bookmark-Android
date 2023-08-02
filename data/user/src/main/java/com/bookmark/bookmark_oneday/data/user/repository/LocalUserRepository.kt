package com.bookmark.bookmark_oneday.data.user.repository

import androidx.datastore.core.DataStore
import com.bookmark.bookmark_oneday.core.datastore.User
import com.bookmark.bookmark_oneday.core.room.database.BookmarkRoomDatabase
import com.bookmark.bookmark_oneday.domain.user.model.UserInfo
import com.bookmark.bookmark_oneday.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserRepository constructor(
    private val dataStore : DataStore<User>
) : UserRepository {
    override fun getGoalReadingTime(): Flow<Int> = dataStore.data.map { user ->
        user.targetReadTime
    }

    override fun getUserProfile(): Flow<UserInfo> = dataStore.data.map { user ->
        UserInfo(
            nickname = user.nickname,
            profileImage = if (user.profileUri == "") null else user.profileUri,
            bio = user.bio
        )
    }

    override suspend fun setUserProfile(nickname: String, profileImage: String?, bio: String) {
        dataStore.updateData { user ->
            user.toBuilder().setNickname(nickname).setBio(bio).setProfileUri(profileImage ?: "").build()
        }
    }

    override suspend fun setReadingTime(readingTime: Int) {
        dataStore.updateData { user ->
            user.toBuilder().setTargetReadTime(readingTime).build()
        }
    }

    override suspend fun clearUser() {
        dataStore.updateData {
            it.toBuilder().clear().build()
        }
        BookmarkRoomDatabase.clearDatabase()
    }
}