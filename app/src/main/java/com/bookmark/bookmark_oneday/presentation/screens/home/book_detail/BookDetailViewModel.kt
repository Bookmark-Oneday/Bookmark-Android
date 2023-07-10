package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.BookState
import com.bookmark.bookmark_oneday.domain.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetBookDetail
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
    @Assisted private val bookId : String
): ViewModel() {

    private val events = Channel<BookDetailEvent>()
    val state : StateFlow<BookDetailState> = events.receiveAsFlow()
        .runningFold(BookDetailState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookDetailState())

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

    fun getBookState(removeState : Boolean = false) : BookState? {
        val bookDetail = state.value.bookDetail ?: return null

        return BookState.fromBookDetail(bookDetail, removeState)
    }

    fun applyChangedReadingHistory(readingHistoryList : List<ReadingHistory>) {
        viewModelScope.launch {
            events.send(BookDetailEvent.UpdateReadingHistory(readingHistoryList))
        }
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