package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseDeleteBook
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove.model.BookDetailRemoveDialogEvent
import com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_remove.model.BookDetailRemoveDialogState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookDetailRemoveDialogViewModel @AssistedInject constructor(
    private val useCaseDeleteBook : UseCaseDeleteBook,
    @Assisted private val bookId : String
) : ViewModel() {

    private val event = Channel<BookDetailRemoveDialogEvent>()
    val state : StateFlow<BookDetailRemoveDialogState> = event.receiveAsFlow()
        .runningFold(BookDetailRemoveDialogState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookDetailRemoveDialogState())

    private val _sideEffectsCloseDialog = MutableSharedFlow<Boolean>()
    val sideEffectsCloseDialog = _sideEffectsCloseDialog.asSharedFlow()

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

    @AssistedFactory
    interface ViewModelAssistedFactory {
        fun create(bookId : String) : BookDetailRemoveDialogViewModel
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