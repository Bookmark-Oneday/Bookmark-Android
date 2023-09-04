package com.bookmark.bookmark_oneday.domain.user.usecase

import com.bookmark.bookmark_oneday.domain.user.repository.UserRepository
import javax.inject.Inject

class UseCaseClearUser @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.clearUser()
    }
}