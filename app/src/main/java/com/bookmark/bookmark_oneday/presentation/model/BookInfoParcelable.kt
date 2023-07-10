package com.bookmark.bookmark_oneday.presentation.model

import android.os.Parcelable
import com.bookmark.bookmark_oneday.domain.book.model.BookItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookInfoParcelable(
    val id: String,
    val thumbnail: String,
    val title: String,
    val author: String,
    val reading: Boolean = false,
    val favorite: Boolean = false
) : Parcelable {
    companion object {
        fun fromBookItem(bookItem: BookItem): BookInfoParcelable {
            return BookInfoParcelable(
                id = bookItem.id,
                thumbnail = bookItem.thumbnail,
                title = bookItem.title,
                author = bookItem.author,
                reading = bookItem.reading,
                favorite = bookItem.favorite
            )
        }
    }

    fun toBookItem(): BookItem {
        return BookItem(
            id = id,
            thumbnail = thumbnail,
            title = title,
            author = author,
            reading = reading,
            favorite = favorite
        )
    }
}

