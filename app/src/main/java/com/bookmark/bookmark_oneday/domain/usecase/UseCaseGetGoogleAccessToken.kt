package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.TokenInfo
import com.bookmark.bookmark_oneday.domain.repository.GoogleLoginRepository
import javax.inject.Inject

class UseCaseGetGoogleAccessToken @Inject constructor(
    private val repository : GoogleLoginRepository
) {
    suspend operator fun invoke(authCode: String) : BaseResponse<TokenInfo> {
        return repository.getAccessToken(authCode)
    }
}