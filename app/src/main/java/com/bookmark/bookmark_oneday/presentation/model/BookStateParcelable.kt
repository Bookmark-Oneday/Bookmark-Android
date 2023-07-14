package com.bookmark.bookmark_oneday.presentation.model

import android.os.Parcelable
import com.bookmark.bookmark_oneday.domain.book.model.BookState
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookStateParcelable(val bookId : String, val isLike : Boolean, val isReading : Boolean, val isRemoved : Boolean) :
    Parcelable {
    companion object {
        fun fromBookState(bookState: BookState) : BookStateParcelable {
            return BookStateParcelable(
                bookId = bookState.bookId,
                isLike = bookState.isLike,
                isReading = bookState.isReading,
                isRemoved = bookState.isRemoved
            )
        }
    }

    fun toBookState() : BookState {
        return BookState(bookId = bookId, isLike = isLike, isReading = isReading, isRemoved = isRemoved)
    }
}