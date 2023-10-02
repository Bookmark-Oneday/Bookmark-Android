package com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingCheckData
import com.bookmark.bookmark_oneday.domain.oneline.usecase.UseCaseDeleteOneline
import com.bookmark.bookmark_oneday.domain.oneline.usecase.UseCaseGetOneline
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.TodayOnelineEvent
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.TodayOnelineSideEffect
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.TodayOnelineState
import com.bookmark.bookmark_oneday.presentation.screens.home.today_oneline.model.ViewPagerPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayOnelineViewModel @Inject constructor(
    private val useCaseGetOneline: UseCaseGetOneline,
    private val useCaseDeleteOneline: UseCaseDeleteOneline
) : ViewModel() {
    private val pagingCheckData = PagingCheckData()

    private val event = Channel<TodayOnelineEvent>()
    val state : StateFlow<TodayOnelineState> = event.receiveAsFlow()
        .runningFold(TodayOnelineState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TodayOnelineState())

    private val _sideEvent = MutableSharedFlow<TodayOnelineSideEffect>()
    val sideEffect = _sideEvent.asSharedFlow()

    // 사용자의 터치 반응 없이 pager 를 이동했는지 여부를 나타냅니다.
    private var moveByResponse = false

    // 현재 페이지 index
    private var currentPageIdx : Int? = null

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
        viewModelScope.launch {
            event.send(TodayOnelineEvent.ChangePagerPosition(pageIdx))
        }
    }

    // MVI 패턴에서 sideEffect 는 Intent 에 의해서만 수행되어야 하지만 (reduce 함수 내),
    // 다음 페이지의 첫 아이멭으로 이동하는 이벤트의 경우, state가 collect 되기 전, 먼저 이 sideEffect 가 collect 된다면
    // outOfIndex 에러가 발생할 가능성이 있어, Activity 의 viewPager 의 ListAdapter 의 submit List 의 콜백으로 호출될 수 있도록
    // 별도의 함수로 분리했습니다.
    fun callMoveSideEffect(itemSize : Int) {
        if (!moveByResponse) return
        moveByResponse = false
        val viewPagerPosition = ViewPagerPosition (position = itemSize / 5 * 5, useAnimation = true)
        callSideEffect(TodayOnelineSideEffect.MovePage(viewPagerPosition))
    }

    fun deleteOneline() {
        val pageIdx = currentPageIdx
        if (pageIdx == null || pageIdx >= state.value.onelineList.size) return

        val id = state.value.onelineList[pageIdx].id
        viewModelScope.launch {
            event.send(TodayOnelineEvent.DeleteLoading)
            val response = useCaseDeleteOneline(id)
            if (response is BaseResponse.EmptySuccess) {
                event.send(TodayOnelineEvent.DeleteSuccess(id))
            } else {
                event.send(TodayOnelineEvent.DeleteFail)
            }
        }
    }

    fun getCurrentOnelineBookIsbn() : String? {
        return state.value.onelineList.getOrNull(currentPageIdx ?: 0)?.bookIsbn
    }

    private fun reduce(state : TodayOnelineState, event: TodayOnelineEvent) : TodayOnelineState {
        return when (event) {
            is TodayOnelineEvent.ChangePagerPosition -> {
                currentPageIdx = event.position
                val userProfile = if(state.onelineList.isEmpty()) null else state.onelineList[event.position].userProfile
                state.copy(userProfile = userProfile)
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
                val loadDataIsEmpty = event.pagingData.dataList.isEmpty()
                if (loadDataIsEmpty) {
                    return state.copy(
                        showLoading = false,
                        showLoadingFail = false,
                        showEmptyView = state.onelineList.isEmpty()
                    )
                }

                val dataList = state.onelineList + event.pagingData.dataList
                val userProfile = dataList[state.onelineList.size].userProfile

                moveByResponse = true
                state.copy(
                    onelineList = dataList,
                    showLoading = false,
                    showLoadingFail = false,
                    userProfile = userProfile,
                    showEmptyView = false
                )
            }
            is TodayOnelineEvent.FirstPageDataLoadingSuccess -> {
                val dataList = event.pagingData.dataList

                state.copy(
                    onelineList = dataList,
                    showLoading = false,
                    showLoadingFail = false,
                    showEmptyView = dataList.isEmpty()
                )
            }
            TodayOnelineEvent.DeleteLoading -> {
                return state.copy(
                    showLoading = true
                )
            }
            TodayOnelineEvent.DeleteFail -> {
                return state.copy()
            }
            is TodayOnelineEvent.DeleteSuccess -> {
                val dataList = state.onelineList.filter { it.id != event.id }
                return state.copy(
                    onelineList = dataList,
                    showLoading = false,
                    showEmptyView = dataList.isEmpty()
                )
            }
        }
    }

    private fun callSideEffect(sideEffect: TodayOnelineSideEffect) {
        viewModelScope.launch {
            _sideEvent.emit(sideEffect)
        }
    }

}