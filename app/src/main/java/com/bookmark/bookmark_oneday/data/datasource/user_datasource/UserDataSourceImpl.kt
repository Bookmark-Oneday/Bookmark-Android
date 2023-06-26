package com.bookmark.bookmark_oneday.data.datasource.user_datasource

import androidx.datastore.core.DataStore
import com.bookmark.bookmark_oneday.User
import com.bookmark.bookmark_oneday.data.models.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserDataSourceImpl @Inject constructor(
    private val userDataStore: DataStore<User>
) : UserDataSource {
    override fun getUser(): Flow<UserEntity> {
        return userDataStore.data.map {
            UserEntity(
                nickname = it.nickname,
                profileUri = if (it.profileUri == "") null else it.profileUri,
                bio = it.bio,
                targetReadTime = it.targetReadTime
            )
        }
    }

    override suspend fun setUser(nickname: String, profileUri: String?, bio: String) {
        userDataStore.updateData { user ->
            user.toBuilder().setNickname(nickname).setBio(bio).setProfileUri(profileUri ?: "").build()
        }
    }

    override suspend fun setReadTime(readTime: Int) {
        userDataStore.updateData { user ->
            user.toBuilder().setTargetReadTime(readTime).build()
        }
    }


}