package com.bookmark.bookmark_oneday.domain.repository

interface ApplicationRepository {
    fun checkFirstExecution() : Boolean
    fun setExecuted()
}