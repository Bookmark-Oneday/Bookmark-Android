package com.bookmark.bookmark_oneday.domain.book.model

data class BookState(val bookId : String, val isLike : Boolean, val isReading : Boolean, val isRemoved : Boolean) {
    companion object {
        fun fromBookDetail(bookDetail: BookDetail, isRemoved : Boolean) : BookState {
            return BookState(
                bookId = bookDetail.bookId,
                isLike = bookDetail.favorite,
                isReading = bookDetail.checkIsReading(),
                isRemoved = isRemoved
            )
        }
    }
}
