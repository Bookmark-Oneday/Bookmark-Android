package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BookDetail
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetBookDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val useCaseGetBookDetail : UseCaseGetBookDetail
): ViewModel() {
    private val events = Channel<BookDetailEvent>()
    val state : StateFlow<BookDetailState> = events.receiveAsFlow()
        .runningFold(BookDetailState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookDetailState())

    fun tryGetBookDetail(bookId : Int) {
        viewModelScope.launch {
            events.send(BookDetailEvent.GetBookDetailLoading)
            val bookDetail = useCaseGetBookDetail.invoke(bookId)
            events.send(BookDetailEvent.GetBookDetailSuccess(bookDetail))
        }
    }

    fun setPageInfo(currentPage : Int, totalPage : Int) {
        viewModelScope.launch {
            events.send(BookDetailEvent.SetPageInfo(currentPage, totalPage))
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
        }
    }
}

data class BookDetailState(
    val bookDetail: BookDetail? = null,
    val toolbarButtonActive : Boolean = false,
    val inputPageButtonActive : Boolean = false,
    val isShowingLoadingView : Boolean = true,
)

sealed class BookDetailEvent {
    object GetBookDetailLoading : BookDetailEvent()
    object GetBookDetailFail : BookDetailEvent()
    class GetBookDetailSuccess(val bookDetail: BookDetail) : BookDetailEvent()
    class SetPageInfo(val currentPage : Int, val totalPage : Int) : BookDetailEvent()
}