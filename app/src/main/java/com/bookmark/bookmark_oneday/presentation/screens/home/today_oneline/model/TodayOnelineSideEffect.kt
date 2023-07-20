package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model

sealed class TodayOnelineSideEffect {
    class MovePage(val viewPagerPosition : ViewPagerPosition) : TodayOnelineSideEffect()
    class ShowToast(val toastMessage : String) : TodayOnelineSideEffect()
}
