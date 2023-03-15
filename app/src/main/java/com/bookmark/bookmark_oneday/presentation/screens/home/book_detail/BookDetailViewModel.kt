package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BookDetail
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseGetBookDetail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookDetailViewModel : ViewModel() {

    private val useCaseGetBookDetail = UseCaseGetBookDetail()

    private val events = Channel<BookDetailEvent>()
    val state : StateFlow<BookDetailState> = events.receiveAsFlow()
        .runningFold(BookDetailState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookDetailState())

    fun tryGetBookDetail() {
        viewModelScope.launch {
            events.send(BookDetailEvent.GetBookDetailLoading)
            val bookDetail = useCaseGetBookDetail.invoke(1)
            delay(1500L)
            events.send(BookDetailEvent.GetBookDetailSuccess(bookDetail))
        }
    }

    fun tryDeleteBook() {
        viewModelScope.launch {
            events.send(BookDetailEvent.DeleteBookLoading)
            delay(1000L)
            events.send(BookDetailEvent.DeleteBookSuccess)
        }
    }

    fun tryEditPageInfo(currentPage : Int, totalPage : Int) {
        viewModelScope.launch {
            events.send(BookDetailEvent.EditPageLoading)
            delay(1000L)
            events.send(BookDetailEvent.EditPageSuccess(currentPage, totalPage))
        }
    }

    private fun reduce(state : BookDetailState, event : BookDetailEvent) : BookDetailState {
        return when(event) {
            BookDetailEvent.GetBookDetailLoading -> {
                state.copy(toolbarButtonActive = false, inputPageButtonActive = false)
            }
            BookDetailEvent.GetBookDetailFail -> {
                state.copy(toolbarButtonActive = false, inputPageButtonActive = false)
            }
            is BookDetailEvent.GetBookDetailSuccess -> {
                BookDetailState(
                    bookDetail = event.bookDetail,
                    toolbarButtonActive = true,
                    inputPageButtonActive = true
                )
            }
            BookDetailEvent.EditPageLoading -> {
                state.copy(inputPageButtonActive = false)
            }
            BookDetailEvent.EditPageFail -> {
                state.copy(inputPageButtonActive = true)
            }
            is BookDetailEvent.EditPageSuccess -> {
                state.copy(
                    bookDetail = state.bookDetail?.copy(
                        totalPage = event.totalPage,
                        currentPage = event.currentPage
                    ),
                    inputPageButtonActive = true
                )
            }
            BookDetailEvent.DeleteBookLoading -> {
                state.copy(toolbarButtonActive = false, inputPageButtonActive = false)
            }
            BookDetailEvent.DeleteBookFail -> {
                state.copy(toolbarButtonActive = true, inputPageButtonActive = true)
            }
            BookDetailEvent.DeleteBookSuccess -> {
                state.copy(toolbarButtonActive = true, inputPageButtonActive = true)
            }
        }
    }
}

data class BookDetailState(
    val bookDetail: BookDetail? = null,
    val toolbarButtonActive : Boolean = false,
    val inputPageButtonActive : Boolean = false,
)

sealed class BookDetailEvent {
    object GetBookDetailLoading : BookDetailEvent()
    object GetBookDetailFail : BookDetailEvent()
    class GetBookDetailSuccess(val bookDetail: BookDetail) : BookDetailEvent()
    object EditPageLoading : BookDetailEvent()
    object EditPageFail : BookDetailEvent()
    class EditPageSuccess(val currentPage : Int, val totalPage : Int) : BookDetailEvent()
    object DeleteBookLoading : BookDetailEvent()
    object DeleteBookFail : BookDetailEvent()
    object DeleteBookSuccess : BookDetailEvent()
}