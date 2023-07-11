package com.bookmark.bookmark_oneday.app.di.data

import androidx.datastore.core.DataStore
import com.bookmark.bookmark_oneday.core.datastore.User
import com.bookmark.bookmark_oneday.data.user.repository.LocalUserRepository
import com.bookmark.bookmark_oneday.domain.user.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {
    @Singleton
    @Provides
    fun provideLocalUserRepository(
        dataStore: DataStore<User>
    ) : UserRepository {
        return LocalUserRepository(dataStore)
    }
}