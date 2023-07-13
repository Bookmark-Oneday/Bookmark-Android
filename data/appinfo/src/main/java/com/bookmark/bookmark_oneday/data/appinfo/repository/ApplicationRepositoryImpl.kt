package com.bookmark.bookmark_oneday.data.appinfo.repository

import com.bookmark.bookmark_oneday.data.appinfo.datasource.ApplicationDataSource
import com.bookmark.bookmark_oneday.domain.appinfo.repository.ApplicationRepository
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor(
    private val dataSource: ApplicationDataSource
) : ApplicationRepository {
    override fun checkFirstExecution(): Boolean {
        return dataSource.getFirstExecution()
    }

    override fun setExecuted() {
        dataSource.setFirstExecution(false)
    }
}