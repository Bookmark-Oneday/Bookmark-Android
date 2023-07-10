package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.model

import com.bookmark.bookmark_oneday.domain.book.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.model.SortData
import com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.MyLibraryViewModel

data class MyLibraryState(
    val bookList : List<MyLibraryItem> = listOf(),
    val footerList : List<MyLibraryItem> = listOf(),
    val showLoadingFail : Boolean = false,
    val pagingLoading : Boolean = false,
    val showPagingLoadingFail : Boolean = false,
    val currentSortData: SortData = MyLibraryViewModel.sortList[0],
    val totalItemCountString : String = "-",
    val sortButtonActive : Boolean = false
)