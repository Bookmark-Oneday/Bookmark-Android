package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.oneline_datasource.OnelineDataSource
import com.bookmark.bookmark_oneday.data.models.dto.OneLineDto
import com.bookmark.bookmark_oneday.data.models.request_body.RegisterOneLineRequestBody
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.OneLine
import com.bookmark.bookmark_oneday.domain.model.OneLineContent
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.domain.repository.OnelineRepository
import javax.inject.Inject

class OnelineRepositoryImpl @Inject constructor(
    private val dataSource: OnelineDataSource
) : OnelineRepository {
    override suspend fun getOnelineList(
        perPage: Int,
        key: String,
        sortType: String
    ): BaseResponse<PagingData<OneLine>> {
        val response = dataSource.getOnelineList(perPage = perPage, continuousToken = key, sortType = sortType)

        if (response is BaseResponse.Failure) {
            return response
        }
        if (response is BaseResponse.Success) {
            val mappingData = response.data.dataList.map { OneLineDto.toOneLine(it) }
            val pagingData = PagingData(
                dataList = mappingData,
                nextPageToken = response.data.nextPageToken,
                totalItemCount = response.data.totalItemCount,
                isFinish = response.data.isFinish
            )
            return BaseResponse.Success(pagingData)
        }

        throw IllegalAccessException("response must be BaseResponse.Failure or BaseResponse.Success")
    }

    override suspend fun registerOneLine(oneLineContent: OneLineContent): BaseResponse<Nothing> {
        val requestBody = RegisterOneLineRequestBody.fromOnelineContent(oneLineContent = oneLineContent)
        return dataSource.registerOneline(requestBody)
    }
}