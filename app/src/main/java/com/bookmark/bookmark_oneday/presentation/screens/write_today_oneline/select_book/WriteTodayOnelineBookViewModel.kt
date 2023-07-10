package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.select_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookItem
import com.bookmark.bookmark_oneday.core.model.PagingCheckData
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetBookList
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.select_book.model.WriteTodayOnelineEvent
import com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.select_book.model.WriteTodayOnelineState
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
class WriteTodayOnelineBookViewModel @Inject constructor(
    private val useCaseGetBookList: UseCaseGetBookList
): ViewModel() {

    private val event = Channel<WriteTodayOnelineEvent>()
    val state: StateFlow<WriteTodayOnelineState> = event.receiveAsFlow()
        .runningFold(WriteTodayOnelineState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, WriteTodayOnelineState())
    private val pagingCheckData = PagingCheckData()


    init {
        tryRefreshPage()
    }

    fun tryRefreshPage() {
        pagingCheckData.setRefresh()
        viewModelScope.launch {
            event.send(WriteTodayOnelineEvent.NextPageLoading)
            val response = useCaseGetBookList.getPage(nextPageKey = "0")

            if (response is BaseResponse.Success) {
                pagingCheckData.setLoadSuccess(response.data.nextPageToken)
                if (response.data.isFinish) pagingCheckData.setFinish()

                val dataList = response.data.dataList
                event.send(WriteTodayOnelineEvent.FirstPageSuccess(dataList))
            } else {
                pagingCheckData.setLoadFail()
                event.send(WriteTodayOnelineEvent.NextPageFailure)
            }

        }
    }

    fun tryNextPage() {
        if (pagingCheckData.tryLoading || pagingCheckData.isFinish) return
        pagingCheckData.setLoading()
        viewModelScope.launch {
            event.send(WriteTodayOnelineEvent.NextPageLoading)
            val response = useCaseGetBookList.getPage(nextPageKey = pagingCheckData.nextKey ?: "0")

            if (response is BaseResponse.Success) {
                pagingCheckData.setLoadSuccess(response.data.nextPageToken)
                if (response.data.isFinish) pagingCheckData.setFinish()

                val dataList = response.data.dataList
                event.send(WriteTodayOnelineEvent.NextPageSuccess(dataList))
            } else {
                pagingCheckData.setLoadFail()
                event.send(WriteTodayOnelineEvent.NextPageFailure)
            }

        }
    }

    private fun reduce(state : WriteTodayOnelineState, event: WriteTodayOnelineEvent) : WriteTodayOnelineState {
        return event.reduce(state)
    }

    fun selectBook(book : BookItem) {
        viewModelScope.launch {
            if (book == state.value.selectedBook) {
                event.send(WriteTodayOnelineEvent.UnSelectBook)
            } else {
                event.send(WriteTodayOnelineEvent.SelectBook(book))
            }
        }
    }
}