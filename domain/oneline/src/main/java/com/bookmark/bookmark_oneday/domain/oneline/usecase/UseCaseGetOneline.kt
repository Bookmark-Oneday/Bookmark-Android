package com.bookmark.bookmark_oneday.domain.oneline.usecase

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.oneline.model.OneLine
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.domain.oneline.repository.OnelineRepository
import javax.inject.Inject

class UseCaseGetOneline @Inject constructor(
    private val repository : OnelineRepository
) {
    suspend operator fun invoke(key : String) : BaseResponse<PagingData<OneLine>> {
        return repository.getOnelineList(key = key)
    }
}