package com.bookmark.bookmark_oneday.presentation.model

sealed class ListItem<out T>(val viewType : Int) {
    object ItemAdder : ListItem<Nothing>(LIST_ADDER)
    class Content<dataType>(val data : dataType) : ListItem<dataType>(LIST_CONTENT)
    object ItemLoading : ListItem<Nothing>(LIST_LOADING)

    companion object {
        const val LIST_ADDER = 0
        const val LIST_CONTENT = 1
        const val LIST_LOADING = 2
    }
}
