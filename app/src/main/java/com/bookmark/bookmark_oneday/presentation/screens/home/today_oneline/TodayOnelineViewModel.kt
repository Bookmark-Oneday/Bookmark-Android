package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.PagingCheckData
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetOneline
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.TodayOnelineEvent
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.TodayOnelineState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayOnelineViewModel @Inject constructor(
    private val useCaseGetOneline: UseCaseGetOneline
) : ViewModel() {
    private val pagingCheckData = PagingCheckData()

    private val event = Channel<TodayOnelineEvent>()
    val state : StateFlow<TodayOnelineState> = event.receiveAsFlow()
        .runningFold(TodayOnelineState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TodayOnelineState())

    init {
        tryGetFirstPagingData()
    }

    fun tryGetFirstPagingData() {
        viewModelScope.launch {
            pagingCheckData.setRefresh()
            event.send(TodayOnelineEvent.DataLoading)
            val response = useCaseGetOneline(key = "0")

            if (response is BaseResponse.Success) {
                pagingCheckData.setLoadSuccess(response.data.nextPageToken)
                if (response.data.isFinish) pagingCheckData.setFinish()

                event.send(TodayOnelineEvent.FirstPageDataLoadingSuccess(response.data))
            } else {
                pagingCheckData.setLoadFail()
                event.send(TodayOnelineEvent.DataLoadingFail)
            }
        }
    }

    fun tryGetNextPagingData() {
        if (pagingCheckData.tryLoading || pagingCheckData.isFinish) return

        viewModelScope.launch {
            event.send(TodayOnelineEvent.DataLoading)
            val response = useCaseGetOneline(key = pagingCheckData.nextKey ?: "0")

            if (response is BaseResponse.Success) {
                pagingCheckData.setLoadSuccess(response.data.nextPageToken)
                if (response.data.isFinish) pagingCheckData.setFinish()

                event.send(TodayOnelineEvent.DataLoadingSuccess(response.data))
            } else {
                pagingCheckData.setLoadFail()
                event.send(TodayOnelineEvent.DataLoadingFail)
            }
        }
    }

    fun setCurrentPage(pageIdx : Int) {
        if (state.value.currentPosition == pageIdx) return

        viewModelScope.launch {
            event.send(TodayOnelineEvent.ChangePagerPosition(pageIdx))
        }
    }

    private fun reduce(state : TodayOnelineState, event: TodayOnelineEvent) : TodayOnelineState {
        return when (event) {
            is TodayOnelineEvent.ChangePagerPosition -> {
                val userProfile = if(state.onelineList.isEmpty()) null else state.onelineList[event.position].userProfile
                state.copy(currentPosition = event.position, userProfile = userProfile)
            }
            TodayOnelineEvent.DataLoading -> {
                state.copy(
                    showLoading = true,
                    showLoadingFail = false
                )
            }
            TodayOnelineEvent.DataLoadingFail -> {
                state.copy(
                    showLoading = false,
                    showLoadingFail = true
                )
            }
            is TodayOnelineEvent.DataLoadingSuccess -> {
                val dataList = state.onelineList + event.pagingData.dataList
                val position = if (event.pagingData.dataList.isEmpty()) {
                    state.currentPosition
                } else {
                    state.onelineList.size
                }
                val userProfile = if (position != null) {
                    dataList[position].userProfile
                } else {
                    null
                }

                state.copy(
                    currentPosition = position,
                    onelineList = dataList,
                    showLoading = false,
                    showLoadingFail = false,
                    userProfile = userProfile

                )
            }
            is TodayOnelineEvent.FirstPageDataLoadingSuccess -> {
                val dataList = event.pagingData.dataList
                val userProfile = dataList[0].userProfile
                state.copy(
                    currentPosition = 0,
                    onelineList = dataList,
                    showLoading = false,
                    showLoadingFail = false,
                    userProfile = userProfile
                )
            }
        }
    }
}