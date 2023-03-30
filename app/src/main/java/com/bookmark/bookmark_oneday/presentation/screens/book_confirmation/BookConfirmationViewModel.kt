package com.bookmark.bookmark_oneday.presentation.screens.book_confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseRegisterBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookConfirmationViewModel @Inject constructor(
    private val useCaseRegisterBook: UseCaseRegisterBook
) : ViewModel() {
    private val events = Channel<BookConfirmEvent>()

    private val _sideEffectShowDuplicateDialog = MutableSharedFlow<Boolean>()
    val sideEffectShowDuplicateDialog = _sideEffectShowDuplicateDialog.asSharedFlow()

    private val _sideEffectSuccessRegister = MutableSharedFlow<Boolean>()
    val sideEffectSuccessRegister = _sideEffectSuccessRegister.asSharedFlow()

    val state : StateFlow<BookConfirmState> = events.receiveAsFlow()
        .runningFold(BookConfirmState(), ::reduce).stateIn(viewModelScope, SharingStarted.Eagerly, BookConfirmState())

    fun applyBookInfo(book: RecognizedBook) {
        viewModelScope.launch {
            events.send(BookConfirmEvent.ChangeBookInfo(book))
        }
    }

    fun cancelRegister() {
        viewModelScope.launch {
            events.send(BookConfirmEvent.BookConfirmEventNormal)
        }
    }

    fun tryRegisterBook() {
        val book = state.value.book ?: return

        viewModelScope.launch {
            events.send(BookConfirmEvent.RegisterBookLoading)

            val response = useCaseRegisterBook(book = book)
            if (response is BaseResponse.Failure) {
                events.send(BookConfirmEvent.RegisterBookFail)
            } else {
                _sideEffectSuccessRegister.emit(true)
            }

        }
    }

    fun tryCheckAndRegisterBook() {
        val book = state.value.book ?: return

        viewModelScope.launch {
            events.send(BookConfirmEvent.RegisterBookLoading)

            val response = useCaseRegisterBook.withDuplicationCheck(book)

            if (response is BaseResponse.Failure) {
                when (response.errorCode) {
                    409 -> {
                        events.send(BookConfirmEvent.RegisterBookDuplicate)
                        _sideEffectShowDuplicateDialog.emit(true)
                    }
                    else -> { events.send(BookConfirmEvent.RegisterBookFail) }
                }
            }
            else {
                tryRegisterBook()
            }

        }
    }

    private fun reduce(state: BookConfirmState, event: BookConfirmEvent) : BookConfirmState {
        return when (event) {
            BookConfirmEvent.RegisterBookLoading -> {
                state.copy(buttonActive = false)
            }
            BookConfirmEvent.RegisterBookDuplicate -> {
                state.copy(buttonActive = false)
            }
            BookConfirmEvent.RegisterBookFail -> {
                state.copy(buttonActive = true)
            }
            is BookConfirmEvent.ChangeBookInfo -> {
                state.copy(book = event.book)
            }
            BookConfirmEvent.BookConfirmEventNormal -> {
                state.copy(buttonActive = true)
            }
        }
    }
}

data class BookConfirmState(
    val book: RecognizedBook ?= null,
    val buttonActive : Boolean = true
)

sealed class BookConfirmEvent {
    object BookConfirmEventNormal : BookConfirmEvent()
    object RegisterBookLoading : BookConfirmEvent()
    object RegisterBookDuplicate : BookConfirmEvent()
    object RegisterBookFail : BookConfirmEvent()
    class ChangeBookInfo(val book: RecognizedBook) : BookConfirmEvent()
}