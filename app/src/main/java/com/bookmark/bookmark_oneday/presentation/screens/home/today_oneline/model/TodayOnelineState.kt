package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model

import com.bookmark.bookmark_oneday.domain.model.OneLine
import com.bookmark.bookmark_oneday.domain.model.UserProfile

data class TodayOnelineState (
    val onelineList : List<OneLine> = listOf(),
    val showLoadingFail : Boolean = false,
    val showLoading : Boolean = false,
    val viewPagerPosition : ViewPagerPosition ?= null,
    val userProfile: UserProfile ?= null
)