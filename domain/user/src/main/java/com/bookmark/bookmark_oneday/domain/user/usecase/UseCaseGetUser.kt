package com.bookmark.bookmark_oneday.domain.user.usecase

import com.bookmark.bookmark_oneday.domain.user.model.UserInfo
import com.bookmark.bookmark_oneday.domain.user.repository.UserRepository
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