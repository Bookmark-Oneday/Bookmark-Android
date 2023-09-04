package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.BookDetail
import com.bookmark.bookmark_oneday.domain.book.model.BookState
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseGetBookDetail
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseToggleBookLike
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.model.BookDetailEvent
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.model.BookDetailState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class BookDetailViewModel @AssistedInject constructor(
    private val useCaseGetBookDetail : UseCaseGetBookDetail,
    private val useCaseToggleBookLike: UseCaseToggleBookLike,
    @Assisted private val bookId : String
): ViewModel() {

    private val events = Channel<BookDetailEvent>()
    val state : StateFlow<BookDetailState> = events.receiveAsFlow()
        .runningFold(BookDetailState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookDetailState())

    // 처음 로드한 책 상세 데이터로, 좋아요 및 읽는 중 여부가 변화했는 지를 확인하기 위해 사용합니다.
    private var initBookDetail  : BookDetail ?= null

    fun tryGetBookDetail() {
        viewModelScope.launch {
            events.send(BookDetailEvent.GetBookDetailLoading)
            val response = useCaseGetBookDetail.invoke(bookId)

            if (response is BaseResponse.Success) {
                events.send(BookDetailEvent.GetBookDetailSuccess(response.data))
            } else {
                events.send(BookDetailEvent.GetBookDetailFail)
            }

        }
    }

    fun setPageInfo(currentPage : Int, totalPage : Int) {
        viewModelScope.launch {
            events.send(BookDetailEvent.SetPageInfo(currentPage, totalPage))
        }
    }

    fun toggleLike() {
        val currentLike = state.value.bookDetail?.favorite ?: return
        viewModelScope.launch {
            events.send(BookDetailEvent.ToggleLikeLoading)
            val response = useCaseToggleBookLike(bookId, !currentLike)
            if (response is BaseResponse.Success) {
                events.send(BookDetailEvent.ToggleLikeSuccess(response.data))
            } else {
                events.send(BookDetailEvent.ToggleLikeFail)
            }
        }
    }

    fun getBookState(removeState : Boolean = false) : BookState? {
        val bookDetail = state.value.bookDetail ?: return null

        return BookState.fromBookDetail(bookDetail, removeState)
    }

    fun applyChangedReadingHistory(readingHistoryList : List<ReadingHistory>) {
        viewModelScope.launch {
            events.send(BookDetailEvent.UpdateReadingHistory(readingHistoryList))
        }
    }

    fun bookLikeChanged() : Boolean {
        val bookDetail = state.value.bookDetail ?: return false

        return bookDetail.favorite != initBookDetail?.favorite
    }

    fun bookReadingChanged() : Boolean {
        val bookDetail = state.value.bookDetail ?: return false

        val currentReading = bookDetail.currentPage != 0
        val initReading = initBookDetail?.currentPage != null && initBookDetail?.currentPage != 0

        return currentReading != initReading
    }

    private fun reduce(state : BookDetailState, event : BookDetailEvent) : BookDetailState {
        return when(event) {
            BookDetailEvent.GetBookDetailLoading -> {
                state.copy(toolbarButtonActive = false, inputPageButtonActive = false, isShowingLoadingView = true)
            }
            BookDetailEvent.GetBookDetailFail -> {
                state.copy(toolbarButtonActive = false, inputPageButtonActive = false, isShowingLoadingView = false)
            }
            is BookDetailEvent.GetBookDetailSuccess -> {
                initBookDetail = event.bookDetail
                BookDetailState(
                    bookDetail = event.bookDetail,
                    toolbarButtonActive = true,
                    inputPageButtonActive = true,
                    isShowingLoadingView = false
                )
            }
            is BookDetailEvent.SetPageInfo -> {
                state.copy(
                    bookDetail = state.bookDetail?.copy(
                        totalPage = event.totalPage,
                        currentPage = event.currentPage
                    ),
                    inputPageButtonActive = true
                )
            }
            is BookDetailEvent.UpdateReadingHistory -> {
                val bookDetail = state.bookDetail?.copy(history = event.readingHistoryList)
                state.copy(
                    bookDetail = bookDetail
                )
            }
            BookDetailEvent.ToggleLikeLoading -> {
                state.copy(toolbarButtonActive = false)
            }
            BookDetailEvent.ToggleLikeFail -> {
                state.copy(toolbarButtonActive = true)
            }
            is BookDetailEvent.ToggleLikeSuccess -> {
                val bookDetail = state.bookDetail?.copy(favorite = event.isLike)
                state.copy(
                    toolbarButtonActive = true,
                    bookDetail = bookDetail
                )
            }
        }
    }

    @AssistedFactory
    interface ViewModelAssistedFactory {
        fun create(bookId : String) : BookDetailViewModel
    }

    companion object {
        fun provideViewModelFactory(
            assistedFactory: ViewModelAssistedFactory,
            bookId : String
        ) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(bookId) as T
            }
        }
    }
}