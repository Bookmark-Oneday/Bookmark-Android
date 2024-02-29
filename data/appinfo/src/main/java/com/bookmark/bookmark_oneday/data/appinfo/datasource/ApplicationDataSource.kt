package com.bookmark.bookmark_oneday.data.appinfo.datasource

interface ApplicationDataSource {
    fun getFirstExecution() : Boolean
    fun setFirstExecution(isFirst : Boolean)
    fun getTimerNotificationUsed() : Boolean
    fun setTimerNotificationUsed(useTimerNotification : Boolean)
}