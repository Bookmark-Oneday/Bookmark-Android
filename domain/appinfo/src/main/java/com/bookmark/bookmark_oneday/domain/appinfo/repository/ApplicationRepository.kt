package com.bookmark.bookmark_oneday.domain.appinfo.repository

interface ApplicationRepository {
    fun checkFirstExecution() : Boolean
    fun setExecuted()
}