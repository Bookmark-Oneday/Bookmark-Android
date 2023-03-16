package com.bookmark.bookmark_oneday.presentation.screens.home.book_detail.component.dialog_editpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmark.bookmark_oneday.domain.usecase.UseCaseEditReadingPage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookDetailEditPageDialogViewModel : ViewModel() {

    private val useCaseEditPage = UseCaseEditReadingPage()

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    private val _totalPage = MutableStateFlow(1)
    val totalPage = _totalPage.asStateFlow()

    private val _sideEffectsCloseDialog = MutableSharedFlow<Boolean>()
    val sideEffectsCloseDialog = _sideEffectsCloseDialog.asSharedFlow()

    private val events = Channel<BookDetailEditPageEvent>()
    val state : StateFlow<BookDetailEditPageDialogState> = events.receiveAsFlow()
        .runningFold(BookDetailEditPageDialogState(), ::reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BookDetailEditPageDialogState())

    fun initPageInfo(currentPage : Int, totalPage : Int) {
        _currentPage.value = currentPage
        _totalPage.value = totalPage
    }

    fun tryEditPageInfo(currentPageString : String, totalPageString : String) {
        val currentPage = currentPageString.toIntOrNull()
        val totalPage = totalPageString.toIntOrNull()

        if (currentPage == null || totalPage == null) return

        if (currentPage !in 0..totalPage) {
            viewModelScope.launch {
                events.send(BookDetailEditPageEvent.EditPageFail)
            }
        }

        viewModelScope.launch {
            events.send(BookDetailEditPageEvent.EditPageLoading)

            val response = useCaseEditPage(currentPage, totalPage)
            if (response) {
                _totalPage.value = totalPage
                _currentPage.value = currentPage

                events.send(BookDetailEditPageEvent.EditPageSuccess)
                _sideEffectsCloseDialog.emit(true)
            } else {
                events.send(BookDetailEditPageEvent.EditPageFail)
            }

        }
    }

    private fun reduce(state : BookDetailEditPageDialogState, event : BookDetailEditPageEvent) : BookDetailEditPageDialogState {
        return when(event) {
            BookDetailEditPageEvent.EditPageLoading -> {
                state.copy(
                    inputButtonActive = false,
                    editTextActive = false,
                    availableClose = false,
                )
            }
            BookDetailEditPageEvent.EditPageFail -> {
                state.copy(
                    inputButtonActive = true,
                    editTextActive = true,
                    availableClose = true,
                )
            }
            BookDetailEditPageEvent.EditPageSuccess -> {
                state.copy(
                    inputButtonActive = true,
                    editTextActive = true,
                    availableClose = true,
                )
            }
        }
    }
}

data class BookDetailEditPageDialogState(
    val inputButtonActive : Boolean = true,
    val editTextActive : Boolean = true,
    val availableClose : Boolean = true
)

sealed class BookDetailEditPageEvent {
    object EditPageLoading : BookDetailEditPageEvent()
    object EditPageFail : BookDetailEditPageEvent()
    object EditPageSuccess : BookDetailEditPageEvent()
}