package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseToggleBookLike @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId : String, isLike : Boolean) = repository.updateBookLike(bookId, isLike)
}