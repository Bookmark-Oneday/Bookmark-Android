package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model

import com.bookmark.bookmark_oneday.domain.oneline.model.OneLine
import com.bookmark.bookmark_oneday.core.model.PagingData

sealed class TodayOnelineEvent {
    object DataLoading : TodayOnelineEvent()
    object DataLoadingFail : TodayOnelineEvent()
    class FirstPageDataLoadingSuccess(val pagingData : PagingData<OneLine>) : TodayOnelineEvent()
    class DataLoadingSuccess(val pagingData : PagingData<OneLine>) : TodayOnelineEvent()
    class ChangePagerPosition(val position : Int) : TodayOnelineEvent()
    object DeleteLoading : TodayOnelineEvent()
    object DeleteFail : TodayOnelineEvent()
    class DeleteSuccess(val id : String) : TodayOnelineEvent()
}