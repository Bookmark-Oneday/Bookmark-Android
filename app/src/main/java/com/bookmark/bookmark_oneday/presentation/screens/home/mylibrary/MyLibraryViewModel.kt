package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.*
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetBookList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLibraryViewModel @Inject constructor(
    private val useCaseGetBookList: UseCaseGetBookList
) : ViewModel() {

    private val event = Channel<MyLibraryEvent>()
    val state : StateFlow<MyLibraryState> = event.receiveAsFlow()
        .runningFold(MyLibraryState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MyLibraryState())

    private val pagingCheckData = PagingCheckData()

    init {
        tryGetInitPagingData()
    }

    fun tryGetInitPagingData(sortData : SortData = sortList[0]) {
        viewModelScope.launch {
            event.send(MyLibraryEvent.InitPagingDataLoading(sortData))

            val response = useCaseGetBookList.loadInitPages(sort = sortData.apiValue)

            if (response is BaseResponse.Success) {
                event.send(MyLibraryEvent.InitPagingDataLoadingSuccess(response.data))
            } else {
                event.send(MyLibraryEvent.InitPagingDataLoadingFail)
            }
        }
    }

    // 다음 페이지 요청할 때, 이미 해당 페이지가 요청중이라면 무시
    fun tryGetNextPagingData() {
        if (pagingCheckData.tryLoading || pagingCheckData.isFinish) return
        viewModelScope.launch {
            event.send(MyLibraryEvent.NextPagingDataLoading)

            val response = useCaseGetBookList(key = pagingCheckData.nextKey ?: "0", sort = state.value.currentSortData.apiValue)

            if (response is BaseResponse.Success) {
                event.send(MyLibraryEvent.NextPagingDataLoadingSuccess(response.data))
            } else {
                event.send(MyLibraryEvent.NextPagingDataLoadingFail)
            }

        }
    }

    fun applyItemChange(bookId : String) {
        viewModelScope.launch {
            event.send(MyLibraryEvent.ChangeBookItemProperty(bookId))
        }
    }

    private fun reduce(state : MyLibraryState, event : MyLibraryEvent) : MyLibraryState {
        return when (event) {
            is MyLibraryEvent.InitPagingDataLoading -> {
                pagingCheckData.setRefresh()
                state.copy(
                    currentSortData = event.sortData,
                    pagingLoading = true,
                    bookList = listOf(),
                    footerList = List(6){ MyLibraryItem.BookLoading },
                    totalItemCountString = "-",
                    sortButtonActive = false
                )
            }
            is MyLibraryEvent.InitPagingDataLoadingFail -> {
                pagingCheckData.setLoadFail()
                state.copy(
                    showLoadingFail = true,
                    totalLoading = false,
                    sortButtonActive = true,
                    footerList = listOf()
                )
            }
            is MyLibraryEvent.InitPagingDataLoadingSuccess -> {
                pagingCheckData.setLoadSuccess(event.pagingData.nextPageToken)
                if (event.pagingData.isFinish) pagingCheckData.setFinish()

                val bookList = listOf(MyLibraryItem.BookAdder) + event.pagingData.dataList
                state.copy(
                    bookList = bookList,
                    totalLoading = false,
                    totalItemCountString = event.pagingData.totalItemCount.toString(),
                    sortButtonActive = true,
                    footerList = listOf()
                )
            }
            is MyLibraryEvent.NextPagingDataLoading -> {
                pagingCheckData.setLoading()
                state.copy(
                    showPagingLoadingFail = false,
                    pagingLoading = true,
                    footerList = List(2){ MyLibraryItem.BookLoading }
                )
            }
            is MyLibraryEvent.NextPagingDataLoadingFail -> {
                pagingCheckData.setLoadFail()
                state.copy(
                    showPagingLoadingFail = true,
                    pagingLoading = false,
                    footerList = listOf()
                )
            }
            is MyLibraryEvent.NextPagingDataLoadingSuccess -> {
                pagingCheckData.setLoadSuccess(event.pagingData.nextPageToken)
                if (event.pagingData.isFinish) pagingCheckData.setFinish()

                val bookList = state.bookList + event.pagingData.dataList
                state.copy(
                    bookList = bookList,
                    pagingLoading = false,
                    footerList = listOf()
                )
            }
            is MyLibraryEvent.ChangeBookItemProperty -> {
                // 테스팅
                val bookList = state.bookList.map { item ->
                    return@map if (item is MyLibraryItem.Book && item.id == event.bookId) {
                        item.copy(reading = true)
                    } else {
                        item
                    }
                }
                state.copy(bookList = bookList)
            }
        }
    }

    companion object {
        val sortList = listOf(
            SortData("최신 순", "latest"),
            SortData("과거 순", "past"),
            SortData("즐겨찾는 책", "favorite"),
            SortData("읽고 있는 책", "reading"),
        )
    }
}

data class MyLibraryState(
    val bookList : List<MyLibraryItem> = listOf(),
    val footerList : List<MyLibraryItem> = listOf(),
    val totalLoading : Boolean = true,
    val showLoadingFail : Boolean = false,
    val pagingLoading : Boolean = false,
    val showPagingLoadingFail : Boolean = false,
    val currentSortData: SortData = MyLibraryViewModel.sortList[0],
    val totalItemCountString : String = "-",
    val sortButtonActive : Boolean = false
)

sealed class MyLibraryEvent {
    class InitPagingDataLoading(val sortData: SortData) : MyLibraryEvent()
    object InitPagingDataLoadingFail : MyLibraryEvent()
    class InitPagingDataLoadingSuccess(val pagingData : PagingData<MyLibraryItem>) : MyLibraryEvent()
    object NextPagingDataLoading : MyLibraryEvent()
    object NextPagingDataLoadingFail : MyLibraryEvent()
    class NextPagingDataLoadingSuccess(val pagingData : PagingData<MyLibraryItem>) : MyLibraryEvent()
    class ChangeBookItemProperty(val bookId : String) : MyLibraryEvent()
}