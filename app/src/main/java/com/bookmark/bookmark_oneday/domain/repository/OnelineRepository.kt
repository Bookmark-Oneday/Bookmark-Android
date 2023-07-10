package com.bookmark.bookmark_oneday.domain.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.OneLine
import com.bookmark.bookmark_oneday.domain.model.OneLineContent
import com.bookmark.bookmark_oneday.core.model.PagingData

interface OnelineRepository {
    suspend fun getOnelineList(perPage : Int= 5, key : String, sortType : String = "latest") : BaseResponse<PagingData<OneLine>>

    suspend fun registerOneLine(oneLineContent: OneLineContent) : BaseResponse<Nothing>
}