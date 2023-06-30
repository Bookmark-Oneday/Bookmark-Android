package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.model.UserInfo
import com.bookmark.bookmark_oneday.domain.repository.UserRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UseCaseGetUser @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getProfile() = userRepository.getUserProfile()
    fun getGoalReadingTime() = userRepository.getGoalReadingTime()
    fun isLogin() = userRepository.getUserProfile().map { userInfo ->
        userInfo == UserInfo.emptyUserInfo
    }
}