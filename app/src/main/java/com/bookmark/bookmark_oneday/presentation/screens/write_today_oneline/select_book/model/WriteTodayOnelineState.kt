package com.bookmark.bookmark_oneday.presentation.screens.write_today_oneline.select_book.model

import com.bookmark.bookmark_oneday.domain.book.model.BookItem
import com.bookmark.bookmark_oneday.presentation.model.ListItem

data class WriteTodayOnelineState(
    val dataList : List<ListItem<BookItem>> = emptyList(),
    val showFailureView : Boolean = false,
    val showLoadingView : Boolean = false,
    val footerList : List<ListItem<BookItem>> = List(6){ ListItem.ItemLoading },
    val selectButtonActive : Boolean = false,
    val selectedBook : BookItem?= null
)
