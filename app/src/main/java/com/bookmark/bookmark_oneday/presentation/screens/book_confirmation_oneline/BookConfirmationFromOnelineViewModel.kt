package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation_oneline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseSearchBookInfo
import com.bookmark.bookmark_oneday.presentation.screens.book_confirmation_oneline.model.BookConfirmOnelineEvent
import com.bookmark.bookmark_oneday.presentation.screens.book_confirmation_oneline.model.BookConfirmOnelineState
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
class BookConfirmationFromOnelineViewModel @Inject constructor(
    private val useCaseSearchBookInfo: UseCaseSearchBookInfo
): ViewModel() {
    private val events = Channel<BookConfirmOnelineEvent>()

    val state : StateFlow<BookConfirmOnelineState> = events.receiveAsFlow()
        .runningFold(BookConfirmOnelineState(), ::reduce).stateIn(viewModelScope, SharingStarted.Eagerly, BookConfirmOnelineState())

    fun searchBook(isbn : String) {
        viewModelScope.launch {
            events.send(BookConfirmOnelineEvent.LoadingBookSearch)
            val response = useCaseSearchBookInfo(isbn)
            if (response is BaseResponse.Success) {
                events.send(BookConfirmOnelineEvent.BookSearchSuccess(response.data))
            } else {
                events.send(BookConfirmOnelineEvent.BookSearchFail)
            }
        }
    }

    private fun reduce(state : BookConfirmOnelineState, event : BookConfirmOnelineEvent) : BookConfirmOnelineState {
        return when (event) {
            BookConfirmOnelineEvent.LoadingBookSearch -> {
                state.copy(showLoading = true, showError = false)
            }
            BookConfirmOnelineEvent.BookSearchFail -> {
                state.copy(showLoading = false, showError = true)
            }
            is BookConfirmOnelineEvent.BookSearchSuccess -> {
                state.copy(recognizedBook = event.recognizedBook, showLoading = false, showError = false)
            }
        }
    }
}