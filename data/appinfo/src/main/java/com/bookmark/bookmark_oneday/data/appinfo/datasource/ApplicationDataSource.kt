package com.bookmark.bookmark_oneday.data.appinfo.datasource

interface ApplicationDataSource {
    fun getFirstExecution() : Boolean
    fun setFirstExecution(isFirst : Boolean)
}