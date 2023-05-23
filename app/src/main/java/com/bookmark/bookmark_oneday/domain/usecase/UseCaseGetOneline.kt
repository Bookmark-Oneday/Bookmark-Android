package com.bookmark.bookmark_oneday.domain.usecase

import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.OneLine
import com.bookmark.bookmark_oneday.domain.model.PagingData
import com.bookmark.bookmark_oneday.domain.repository.OnelineRepository
import javax.inject.Inject

class UseCaseGetOneline @Inject constructor(
    private val repository : OnelineRepository
) {
    suspend operator fun invoke(key : String) : BaseResponse<PagingData<OneLine>> {
        return repository.getOnelineList(key = key)
    }
}