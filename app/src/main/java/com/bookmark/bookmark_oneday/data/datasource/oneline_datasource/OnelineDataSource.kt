package com.bookmark.bookmark_oneday.data.datasource.oneline_datasource

import com.bookmark.bookmark_oneday.data.models.dto.OneLineDto
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.PagingData

interface OnelineDataSource {
    suspend fun getOnelineList(perPage : Int, continuousToken : String, sortType : String) : BaseResponse<PagingData<OneLineDto>>
}