package com.bookmark.bookmark_oneday.domain.user.usecase

import com.bookmark.bookmark_oneday.domain.user.repository.UserRepository
import javax.inject.Inject

class UseCaseSetUser @Inject constructor(
    private val userRepository: UserRepository
) {
   suspend fun setUserProfile(nickname : String, profileUri : String?, bio : String) {
       userRepository.setUserProfile(nickname, profileUri, bio)
   }

    suspend fun setReadingTime(readingTime : Int) {
        userRepository.setReadingTime(readingTime)
    }
}