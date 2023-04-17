package com.bookmark.bookmark_oneday.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookState(val bookId : String, val isLike : Boolean, val isReading : Boolean, val isRemoved : Boolean) : Parcelable {
    companion object {
        fun fromBookDetail(bookDetail: BookDetail, isRemoved : Boolean) : BookState {
            return BookState(
                bookId = bookDetail.bookId,
                isLike = false,
                isReading = bookDetail.checkIsReading(),
                isRemoved = isRemoved
            )
        }
    }
}
