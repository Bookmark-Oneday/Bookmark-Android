package com.bookmark.bookmark_oneday.data.datasource.application_datasource

interface ApplicationDataSource {
    fun getFirstExecution() : Boolean
    fun setFirstExecution(isFirst : Boolean)
}