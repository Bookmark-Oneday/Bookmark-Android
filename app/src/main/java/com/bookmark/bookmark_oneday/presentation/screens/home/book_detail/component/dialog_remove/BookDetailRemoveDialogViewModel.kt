package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseDeleteBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailRemoveDialogViewModel @Inject constructor(
    private val useCaseDeleteBook : UseCaseDeleteBook
) : ViewModel() {

    private val event = Channel<BookDetailRemoveDialogEvent>()
    val state : StateFlow<BookDetailRemoveDialogState> = event.receiveAsFlow()
        .runningFold(BookDetailRemoveDialogState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookDetailRemoveDialogState())

    private val _sideEffectsCloseDialog = MutableSharedFlow<Boolean>()
    val sideEffectsCloseDialog = _sideEffectsCloseDialog.asSharedFlow()

    private var bookId = ""

    fun setBookId(bookId : String) {
        this.bookId = bookId
    }

    fun tryDeleteBook() {
        viewModelScope.launch {
            event.send(BookDetailRemoveDialogEvent.RemoveLoading)
            val response = useCaseDeleteBook(bookId)
            if (response) {
                event.send(BookDetailRemoveDialogEvent.RemoveSuccess)
                _sideEffectsCloseDialog.emit(true)
            } else {
                event.send(BookDetailRemoveDialogEvent.RemoveFail)
            }

        }
    }

    private fun reduce(state : BookDetailRemoveDialogState, event : BookDetailRemoveDialogEvent) : BookDetailRemoveDialogState {
        return when (event) {
            BookDetailRemoveDialogEvent.RemoveLoading -> {
                state.copy(
                    buttonActive = false,
                    availableClose = false,
                    showLoadingProgressBar = true,
                )
            }
            BookDetailRemoveDialogEvent.RemoveFail -> {
                state.copy(
                    buttonActive = true,
                    availableClose = true,
                    showLoadingProgressBar = false,
                )
            }
            BookDetailRemoveDialogEvent.RemoveSuccess -> {
                state.copy(
                    buttonActive = true,
                    availableClose = true,
                    showLoadingProgressBar = false,
                )
            }
        }
    }
}

data class BookDetailRemoveDialogState(
    val buttonActive : Boolean = true,
    val availableClose : Boolean = true,
    val showLoadingProgressBar : Boolean = false
)

sealed class BookDetailRemoveDialogEvent {
    object RemoveLoading : BookDetailRemoveDialogEvent()
    object RemoveFail : BookDetailRemoveDialogEvent()
    object RemoveSuccess : BookDetailRemoveDialogEvent()
}