package com.bookmark.bookmark_oneday.data.datasource.oneline_datasource

import com.bookmark.bookmark_oneday.data.models.dto.OneLineDto
import com.bookmark.bookmark_oneday.data.models.request_body.RegisterOneLineRequestBody
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData

interface OnelineDataSource {
    suspend fun getOnelineList(perPage : Int, continuousToken : String, sortType : String) : BaseResponse<PagingData<OneLineDto>>

    suspend fun registerOneline(oneLineContent: RegisterOneLineRequestBody) : BaseResponse<Nothing>
}