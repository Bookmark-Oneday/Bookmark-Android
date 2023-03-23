package com.bookmark.bookmark_oneday.domain.model

sealed class MyLibraryItem(val viewType : Int) {
    object BookAdder : MyLibraryItem(BOOK_ADDER)
    data class Book(
        val id : Int,
        val thumbnail : String,
        val title : String,
        val author : String,
        val reading : Boolean = false,
        val favorite : Boolean = false
    ) : MyLibraryItem(BOOK_NORMAL)

    companion object {
        const val BOOK_ADDER = 0
        const val BOOK_NORMAL = 1
    }

}