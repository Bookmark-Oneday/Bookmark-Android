package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.OneLineContent
import com.bookmark.bookmark_oneday.domain.repository.OnelineRepository
import javax.inject.Inject

class UseCaseRegisterOneLine @Inject constructor(
    private val repository : OnelineRepository
) {
    suspend operator fun invoke(oneLineContent: OneLineContent) : BaseResponse<Nothing> {
        return repository.registerOneLine(oneLineContent)
    }
}