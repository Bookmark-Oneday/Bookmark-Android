package com.bookmark.bookmark_oneday.data.oneline.datasource

import com.bookmark.bookmark_oneday.data.oneline.model.dto.OneLineDto
import com.bookmark.bookmark_oneday.data.oneline.model.request.RegisterOneLineRequestBody
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData

interface OnelineDataSource {
    suspend fun getOnelineList(perPage : Int, continuousToken : String, sortType : String) : BaseResponse<PagingData<OneLineDto>>

    suspend fun registerOneline(oneLineContent: RegisterOneLineRequestBody) : BaseResponse<Nothing>
}