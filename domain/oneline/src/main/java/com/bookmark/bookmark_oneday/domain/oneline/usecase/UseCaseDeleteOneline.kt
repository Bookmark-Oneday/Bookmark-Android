package com.bookmark.bookmark_oneday.domain.oneline.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.oneline.repository.OnelineRepository
import javax.inject.Inject

class UseCaseDeleteOneline @Inject constructor(
    private val repository: OnelineRepository
) {
    suspend operator fun invoke(id : String) : BaseResponse<Nothing> {
        return repository.deleteOneline(id)
    }
}