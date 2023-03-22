package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.model.PagingCheckData
import com.bookmark.bookmark_oneday.domain.model.PagingData
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetBookList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyLibraryViewModel constructor(
    private val useCaseGetBookList: UseCaseGetBookList = UseCaseGetBookList()
) : ViewModel() {

    private val event = Channel<MyLibraryEvent>()
    val state : StateFlow<MyLibraryState> = event.receiveAsFlow()
        .runningFold(MyLibraryState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MyLibraryState())

    private val pagingCheckData = PagingCheckData()

    fun tryGetInitPagingData() {
        viewModelScope.launch {
            event.send(MyLibraryEvent.InitPagingDataLoading)

            val response = useCaseGetBookList.loadInitPages()

            if (response is BaseResponse.Success) {
                event.send(MyLibraryEvent.InitPagingDataLoadingSuccess(response.data))
            } else {
                event.send(MyLibraryEvent.InitPagingDataLoadingFail)
            }

        }
    }

    // 다음 페이지 요청할 때, 이미 해당 페이지가 요청중이라면 무시해야 한다.
    fun tryGetNextPagingData() {
        if (pagingCheckData.tryLoading || pagingCheckData.isFinish) return
        viewModelScope.launch {
            event.send(MyLibraryEvent.NextPagingDataLoading)

            val response = useCaseGetBookList(pagingCheckData.nextKey ?: "0")

            if (response is BaseResponse.Success) {
                event.send(MyLibraryEvent.NextPagingDataLoadingSuccess(response.data))
            } else {
                event.send(MyLibraryEvent.NextPagingDataLoadingFail)
            }

        }
    }

    private fun reduce(state : MyLibraryState, event : MyLibraryEvent) : MyLibraryState {
        return when (event) {
            MyLibraryEvent.InitPagingDataLoading -> {
                pagingCheckData.setLoading()
                MyLibraryState()
            }
            MyLibraryEvent.InitPagingDataLoadingFail -> {
                pagingCheckData.setLoadFail()
                state.copy(showLoadingFail = true, totalLoading = false)
            }
            is MyLibraryEvent.InitPagingDataLoadingSuccess -> {
                pagingCheckData.setLoadSuccess(event.pagingData.nextPageToken)
                if (event.pagingData.isFinish) pagingCheckData.setFinish()

                val bookList = listOf(MyLibraryItem.BookAdder) + event.pagingData.dataList
                state.copy(bookList = bookList, totalLoading = false)
            }
            MyLibraryEvent.NextPagingDataLoading -> {
                pagingCheckData.setLoading()
                state.copy(showPagingLoadingFail = false, pagingLoading = true)
            }
            MyLibraryEvent.NextPagingDataLoadingFail -> {
                pagingCheckData.setLoadFail()
                state.copy(showPagingLoadingFail = true, pagingLoading = false)
            }
            is MyLibraryEvent.NextPagingDataLoadingSuccess -> {
                pagingCheckData.setLoadSuccess(event.pagingData.nextPageToken)
                if (event.pagingData.isFinish) pagingCheckData.setFinish()

                val bookList = state.bookList + event.pagingData.dataList
                state.copy(bookList = bookList, pagingLoading = false)
            }
        }
    }

}

data class MyLibraryState(
    val bookList : List<MyLibraryItem> = listOf(),
    val totalLoading : Boolean = true,
    val showLoadingFail : Boolean = false,
    val pagingLoading : Boolean = false,
    val showPagingLoadingFail : Boolean = false,
)

sealed class MyLibraryEvent {
    object InitPagingDataLoading : MyLibraryEvent()
    object InitPagingDataLoadingFail : MyLibraryEvent()
    class InitPagingDataLoadingSuccess(val pagingData : PagingData<MyLibraryItem>) : MyLibraryEvent()
    object NextPagingDataLoading : MyLibraryEvent()
    object NextPagingDataLoadingFail : MyLibraryEvent()
    class NextPagingDataLoadingSuccess(val pagingData : PagingData<MyLibraryItem>) : MyLibraryEvent()
}