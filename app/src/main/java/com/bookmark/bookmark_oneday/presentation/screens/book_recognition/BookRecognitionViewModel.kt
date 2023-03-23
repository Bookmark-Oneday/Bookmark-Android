package com.bookmark.bookmark_oneday.presentation.screens.book_recognition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseSearchBookInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookRecognitionViewModel constructor(
    private val useCaseSearchBookInfo: UseCaseSearchBookInfo = UseCaseSearchBookInfo()
) : ViewModel() {

    private val events = Channel<BookRecognitionEvent>()
    val state : StateFlow<BookRecognitionState> = events.receiveAsFlow()
        .runningFold(BookRecognitionState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookRecognitionState())

    private val _sideEffectsSearchBookSuccess = MutableSharedFlow<RecognizedBook>()
    val sideEffectsSearchBookSuccess = _sideEffectsSearchBookSuccess.asSharedFlow()

    fun trySearchAndGetBookInfo(isbn : String) {
        // 이미 책 검색을 수행하고 있다면 즉시 리턴
        if (state.value.showLoadingDialog || state.value.showErrorDialog) return

        viewModelScope.launch {
            events.send(BookRecognitionEvent.SearchBookInfoLoading)
            val response = useCaseSearchBookInfo(isbn)

            if (response is BaseResponse.Success) {
                _sideEffectsSearchBookSuccess.emit(response.data)
            } else {
                events.send(BookRecognitionEvent.SearchBookInfoFail)
            }
        }
    }

    fun setScannable() {
        viewModelScope.launch {
            events.send(BookRecognitionEvent.SearchingBarcode)
        }
    }

    private fun reduce(state: BookRecognitionState, event: BookRecognitionEvent)
            : BookRecognitionState {
        return when (event) {
            BookRecognitionEvent.SearchBookInfoLoading -> {
                state.copy(buttonActive = false, showLoadingDialog = true, showErrorDialog = false)
            }
            BookRecognitionEvent.SearchBookInfoFail -> {
                state.copy(buttonActive = true, showLoadingDialog = false, showErrorDialog = true)
            }
            BookRecognitionEvent.SearchingBarcode -> {
                state.copy(buttonActive = true, showLoadingDialog = false, showErrorDialog = false)
            }
        }
    }
}

data class BookRecognitionState(
    val buttonActive : Boolean = true,
    val showLoadingDialog : Boolean = false,
    val showErrorDialog : Boolean = false
)

sealed class BookRecognitionEvent {
    object SearchBookInfoLoading : BookRecognitionEvent()
    object SearchBookInfoFail : BookRecognitionEvent()
    object SearchingBarcode : BookRecognitionEvent()
}