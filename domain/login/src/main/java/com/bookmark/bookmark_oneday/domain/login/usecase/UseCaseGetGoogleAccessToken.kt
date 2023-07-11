package com.bookmark.bookmark_oneday.domain.login.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.login.model.TokenInfo
import com.bookmark.bookmark_oneday.domain.login.repository.GoogleLoginRepository
import javax.inject.Inject

class UseCaseGetGoogleAccessToken @Inject constructor(
    private val repository : GoogleLoginRepository
) {
    suspend operator fun invoke(authCode: String) : BaseResponse<TokenInfo> {
        return repository.getAccessToken(authCode)
    }
}