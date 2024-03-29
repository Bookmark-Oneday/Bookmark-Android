package com.bookmark.bookmark_oneday.presentation.screens.home.mylibrary.model

import com.bookmark.bookmark_oneday.domain.book.model.BookState
import com.bookmark.bookmark_oneday.domain.book.model.MyLibraryItem
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.presentation.model.SortData

sealed class MyLibraryEvent {
    class InitPagingDataLoading(val sortData: SortData) : MyLibraryEvent()
    object InitPagingDataLoadingFail : MyLibraryEvent()
    class InitPagingDataLoadingSuccess(val pagingData : PagingData<MyLibraryItem>) : MyLibraryEvent()
    object NextPagingDataLoading : MyLibraryEvent()
    object NextPagingDataLoadingFail : MyLibraryEvent()
    class NextPagingDataLoadingSuccess(val pagingData : PagingData<MyLibraryItem>) : MyLibraryEvent()
    class ChangeBookItemProperty(val bookState : BookState) : MyLibraryEvent()
    class RemoveBookItem(val bookId : String) : MyLibraryEvent()
}