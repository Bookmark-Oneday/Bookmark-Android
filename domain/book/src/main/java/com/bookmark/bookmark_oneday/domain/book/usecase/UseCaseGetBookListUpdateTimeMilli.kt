package com.bookmark.bookmark_oneday.domain.book.usecase

import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import javax.inject.Inject

class UseCaseGetBookListUpdateTimeMilli @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke() = repository.lastUpdateTimeMilli()
}