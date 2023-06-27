package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.repository.UserRepository
import javax.inject.Inject

class UseCaseGetUser @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getProfile() = userRepository.getUserProfile()
}