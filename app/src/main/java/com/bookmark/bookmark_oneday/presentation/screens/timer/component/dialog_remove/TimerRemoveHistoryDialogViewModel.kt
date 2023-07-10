package com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.book.usecase.UseCaseDeleteHistory
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.model.TimerRemoveHistoryDialogEvent
import com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove.model.TimerRemoveHistoryDialogState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class TimerRemoveHistoryDialogViewModel @AssistedInject constructor(
    private val useCaseDeleteHistory: UseCaseDeleteHistory,
    @Assisted private val bookId: String
) : ViewModel() {

    private val event = Channel<TimerRemoveHistoryDialogEvent>()
    val state : StateFlow<TimerRemoveHistoryDialogState> = event.receiveAsFlow()
        .runningFold(TimerRemoveHistoryDialogState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerRemoveHistoryDialogState())

    private val _sideEffectsNewReadingInfo = MutableSharedFlow<ReadingInfo>()
    val sideEffectsNewReadingInfo = _sideEffectsNewReadingInfo.asSharedFlow()

    fun tryRemoveHistory(targetIdx : String?) {
        viewModelScope.launch {
            event.send(TimerRemoveHistoryDialogEvent.RemoveLoading)

            val response = if (targetIdx != null) {
                useCaseDeleteHistory(bookId, targetIdx)
            } else {
                useCaseDeleteHistory.deleteAll(bookId)
            }

            if (response is BaseResponse.Success) {
                event.send(TimerRemoveHistoryDialogEvent.RemoveSuccess)
                _sideEffectsNewReadingInfo.emit(response.data)
            } else {
                event.send(TimerRemoveHistoryDialogEvent.RemoveFail)
            }
        }
    }

    private fun reduce(state : TimerRemoveHistoryDialogState, event: TimerRemoveHistoryDialogEvent) : TimerRemoveHistoryDialogState {
        return when (event) {
            TimerRemoveHistoryDialogEvent.RemoveLoading -> {
                state.copy(
                    buttonActive = false,
                    availableClose = false,
                    showLoadingProgressBar = true,
                )
            }
            TimerRemoveHistoryDialogEvent.RemoveFail -> {
                state.copy(
                    buttonActive = true,
                    availableClose = true,
                    showLoadingProgressBar = false,
                )
            }
            TimerRemoveHistoryDialogEvent.RemoveSuccess -> {
                state.copy(
                    buttonActive = true,
                    availableClose = true,
                    showLoadingProgressBar = false,
                )
            }
        }
    }

    @AssistedFactory
    interface AssistedViewModelFactory {
        fun create(bookId : String) : TimerRemoveHistoryDialogViewModel
    }

    companion object {
        fun provideViewModelFactory(
            assistedFactory : AssistedViewModelFactory,
            bookId : String
        ) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(bookId) as T
            }
        }
    }
}