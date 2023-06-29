package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.user_datasource.UserDataSource
import com.bookmark.bookmark_oneday.domain.model.UserInfo
import com.bookmark.bookmark_oneday.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override fun getUserProfile(): Flow<UserInfo> = userDataSource.getUser().map { userEntity ->
        UserInfo(nickname = userEntity.nickname, profileImage = userEntity.profileUri, bio = userEntity.bio)
    }

    override suspend fun setUserProfile(nickname: String, profileImage: String?, bio: String) {
        userDataSource.setUser(nickname, profileImage, bio)
    }

    override suspend fun setReadingTime(readingTime: Int) {
        userDataSource.setReadTime(readingTime)
    }

    override suspend fun clearUser() {
        userDataSource.clearUser()
    }

}