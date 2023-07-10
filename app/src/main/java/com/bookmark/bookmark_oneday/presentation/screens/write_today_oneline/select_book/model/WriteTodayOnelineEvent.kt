package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.select_book.model

import com.bookmark.bookmark_oneday.domain.book.model.BookItem
import com.bookmark.bookmark_oneday.presentation.model.ListItem

sealed class WriteTodayOnelineEvent {
    abstract fun reduce(state : WriteTodayOnelineState) : WriteTodayOnelineState

    object NextPageLoading : WriteTodayOnelineEvent() {
        override fun reduce(state: WriteTodayOnelineState): WriteTodayOnelineState {
            return state.copy(
                showFailureView = false,
                showLoadingView = true,
                footerList = List(1){ListItem.ItemLoading}
            )
        }
    }

    object NextPageFailure : WriteTodayOnelineEvent() {
        override fun reduce(state: WriteTodayOnelineState): WriteTodayOnelineState {
            return state.copy(showFailureView = true, showLoadingView = false)
        }
    }

    class NextPageSuccess(val data : List<BookItem>) : WriteTodayOnelineEvent() {
        override fun reduce(state: WriteTodayOnelineState): WriteTodayOnelineState {
            return state.copy(
                showFailureView = false,
                showLoadingView = false,
                dataList = state.dataList + data.map{ ListItem.Content(it) },
                footerList = emptyList()
            )
        }
    }

    class FirstPageSuccess(val data : List<BookItem>) : WriteTodayOnelineEvent() {
        override fun reduce(state: WriteTodayOnelineState): WriteTodayOnelineState {
            return state.copy(
                showFailureView = false,
                showLoadingView = false,
                dataList = listOf(ListItem.ItemAdder) + data.map { ListItem.Content(it) },
                footerList = emptyList()
            )
        }
    }

    class SelectBook(val selectedBook : BookItem) : WriteTodayOnelineEvent()  {
        override fun reduce(state: WriteTodayOnelineState): WriteTodayOnelineState {
            return state.copy(
                selectButtonActive = true,
                selectedBook = selectedBook
            )
        }
    }

    object UnSelectBook : WriteTodayOnelineEvent()  {
        override fun reduce(state: WriteTodayOnelineState): WriteTodayOnelineState {
            return state.copy(
                selectButtonActive = false,
                selectedBook = null
            )
        }
    }
}