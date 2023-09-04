package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingCheckData
import com.bookmark.bookmark_oneday.domain.book.model.BookState
import com.bookmark.bookmark_oneday.domain.book.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseGetBookList
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseGetBookListUpdateTimeMilli
import com.bookmark.bookmark_oneday.domain.user.usecase.UseCaseGetUser
import com.bookmark.bookmark_oneday.presentation.model.SortData
import com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.model.MyLibraryEvent
import com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.model.MyLibraryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MyLibraryViewModel @Inject constructor(
    private val useCaseGetBookList: UseCaseGetBookList,
    useCaseGetUser: UseCaseGetUser,
    useCaseGetBookListUpdateTimeMilli: UseCaseGetBookListUpdateTimeMilli
) : ViewModel() {

    private val event = Channel<MyLibraryEvent>()
    val state : StateFlow<MyLibraryState> = event.receiveAsFlow()
        .runningFold(MyLibraryState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MyLibraryState())

    private val pagingCheckData = PagingCheckData()

    val userProfile = useCaseGetUser.getProfile()

    private var lastLoadBookListTimeMilli = -1L

    init {
        viewModelScope.launch {
            useCaseGetBookListUpdateTimeMilli().collectLatest { lastUpdateBookListTimeMilli ->
                if (lastUpdateBookListTimeMilli > lastLoadBookListTimeMilli) {
                    tryGetInitPagingData(state.value.currentSortData)
                }
            }
        }
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

    /*
     * 책 상세 화면에서 좋아요 및 읽는 중 여부를 변경했을 때,
     * 현재 정렬 기준이 '즐겨찾는 책' 혹은 '읽고 있는 책' 인 경우
     * 전체 목록을 다시 로드해야 하는데, 이 여부를 확인하기 위한 함수입니다.
     */
    fun refreshIfSortTypeIsMatched(readingChanged : Boolean, likeChanged : Boolean) {
        val currentSort = state.value.currentSortData
        if (
            readingChanged && currentSort.apiValue == "reading" ||
            likeChanged && currentSort.apiValue == "favorite"
        ) {
            tryGetInitPagingData(currentSort)
        }
    }

    fun applyItemChange(bookState: BookState) {
        viewModelScope.launch {
            if (!bookState.isRemoved)
                event.send(MyLibraryEvent.ChangeBookItemProperty(bookState))
            else
                event.send(MyLibraryEvent.RemoveBookItem(bookState.bookId))
        }
    }

    private fun reduce(state : MyLibraryState, event : MyLibraryEvent) : MyLibraryState {
        return when (event) {
            is MyLibraryEvent.InitPagingDataLoading -> {
                pagingCheckData.setRefresh()
                state.copy(
                    showLoadingFail = false,
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
                    sortButtonActive = true,
                    footerList = listOf()
                )
            }
            is MyLibraryEvent.InitPagingDataLoadingSuccess -> {
                pagingCheckData.setLoadSuccess(event.pagingData.nextPageToken)
                if (event.pagingData.isFinish) pagingCheckData.setFinish()

                val bookList = listOf(MyLibraryItem.BookAdder) + event.pagingData.dataList
                lastLoadBookListTimeMilli = Calendar.getInstance().timeInMillis
                state.copy(
                    showLoadingFail = false,
                    bookList = bookList,
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
                val bookState = event.bookState
                val bookList = state.bookList.map { item ->
                    return@map if (item is MyLibraryItem.Book && item.id == bookState.bookId) {
                        item.copy(reading = bookState.isReading, favorite = bookState.isLike)
                    } else {
                        item
                    }
                }
                state.copy(bookList = bookList)
            }
            is MyLibraryEvent.RemoveBookItem -> {
                val bookList = state.bookList.filter {
                    it !is MyLibraryItem.Book || it.id != event.bookId
                }
                state.copy(
                    bookList = bookList,
                    totalItemCountString = "${state.totalItemCountString.toInt() - 1}"
                )
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
