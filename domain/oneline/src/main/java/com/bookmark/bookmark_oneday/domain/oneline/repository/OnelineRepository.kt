package com.bookmark.bookmark_oneday.domain.oneline.repository

import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.oneline.model.OneLine
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.domain.oneline.model.OneLineContent

interface OnelineRepository {
    suspend fun getOnelineList(perPage : Int= 5, key : String, sortType : String = "latest") : BaseResponse<PagingData<OneLine>>
    suspend fun registerOneLine(oneLineContent: OneLineContent) : BaseResponse<Nothing>
    suspend fun deleteOneline(id : String) : BaseResponse<Nothing>
}