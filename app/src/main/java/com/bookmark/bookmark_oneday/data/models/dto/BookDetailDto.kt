package com.bookmark.bookmark_oneday.data.models.dto

import com.bookmark.bookmark_oneday.domain.model.BookDetail

data class BookDetailDto(
    val book_id : String,
    val title : String,
    val authors : List<String>,
    val translators : List<String>,
    val publisher : String,
    val titleImage : String,
    val current_page : Int,
    val total_page : Int,
    val history : List<HistoryDto>
) {
    companion object {
        fun toBookDetail(bookDetailDto: BookDetailDto) : BookDetail {
            return BookDetail(
                bookId = bookDetailDto.book_id.toIntOrNull() ?: 1,
                title = bookDetailDto.title,
                author = bookDetailDto.authors.joinToString(","),
                translators = bookDetailDto.translators.joinToString(","),
                publisher = bookDetailDto.publisher,
                imageUrl = bookDetailDto.titleImage,
                currentPage = bookDetailDto.current_page,
                totalPage = bookDetailDto.total_page,
                history = bookDetailDto.history.map { HistoryDto.toReadingHistory(it) }
            )
        }

    }
}
