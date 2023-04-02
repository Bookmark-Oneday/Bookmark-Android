package com.bookmark.bookmark_oneday.domain.model

sealed class MyLibraryItem(val viewType : Int) {
    object BookAdder : MyLibraryItem(BOOK_ADDER)
    data class Book(
        val id : String,
        val thumbnail : String,
        val title : String,
        val author : String,
        val reading : Boolean = false,
        val favorite : Boolean = false
    ) : MyLibraryItem(BOOK_NORMAL)

    object BookLoading : MyLibraryItem(BOOK_LOADING)

    companion object {
        const val BOOK_ADDER = 0
        const val BOOK_NORMAL = 1
        const val BOOK_LOADING = 2
    }

}