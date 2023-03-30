package com.bookmark.bookmark_oneday.presentation.screens.timer.component.dialog_remove

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseDeleteHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerRemoveHistoryDialogViewModel @Inject constructor(
    private val useCaseDeleteHistory: UseCaseDeleteHistory
) : ViewModel() {

    private val event = Channel<TimerRemoveHistoryDialogEvent>()
    val state : StateFlow<TimerRemoveHistoryDialogState> = event.receiveAsFlow()
        .runningFold(TimerRemoveHistoryDialogState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerRemoveHistoryDialogState())

    private val _sideEffectsCloseDialog = MutableSharedFlow<Boolean>()
    val sideEffectsCloseDialog = _sideEffectsCloseDialog.asSharedFlow()

    fun tryRemoveHistory(targetIdx : Int?) {
        viewModelScope.launch {
            event.send(TimerRemoveHistoryDialogEvent.RemoveLoading)

            val result = if (targetIdx != null) {
                useCaseDeleteHistory(targetIdx)
            } else {
                useCaseDeleteHistory.deleteAll()
            }

            if (result) {
                event.send(TimerRemoveHistoryDialogEvent.RemoveSuccess)
                _sideEffectsCloseDialog.emit(true)
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
}

data class TimerRemoveHistoryDialogState(
    val buttonActive : Boolean = true,
    val availableClose : Boolean = true,
    val showLoadingProgressBar : Boolean = false
)

sealed class TimerRemoveHistoryDialogEvent {
    object RemoveLoading : TimerRemoveHistoryDialogEvent()
    object RemoveFail : TimerRemoveHistoryDialogEvent()
    object RemoveSuccess : TimerRemoveHistoryDialogEvent()
}
