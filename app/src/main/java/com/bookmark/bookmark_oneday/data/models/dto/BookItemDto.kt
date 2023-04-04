package com.bookmark.bookmark_oneday.data.models.dto

import com.bookmark.bookmark_oneday.domain.model.MyLibraryItem

data class BookItemDto(
    val book_id : String,
    val title : String,
    val authors : List<String>,
    val translators : List<String>,
    val publisher : String,
    val titleImage : String,
    val reading : Boolean,
    val favorite : Boolean
) {
    companion object {
        fun toMyLibraryBookItem(bookItemDto: BookItemDto) : MyLibraryItem.Book {
            return MyLibraryItem.Book(
                id = bookItemDto.book_id,
                thumbnail = bookItemDto.titleImage,
                title = bookItemDto.title,
                author = bookItemDto.authors.joinToString(","),
                reading = bookItemDto.reading,
                favorite = bookItemDto.favorite
            )
        }

    }
}