package com.bookmark.bookmark_oneday.data.models.request_body

import com.bookmark.bookmark_oneday.domain.model.RecognizedBook

data class RegisterBookRequestBody(
    val title : String,
    val content : String,
    val authors : List<String>,
    val publisher : String,
    val translators : List<String>,
    val thumbnail_url : String,
    val isbn : String,
    val total_page : Int,
    val meta : String,
) {
    companion object {
        fun from(recognizedBook: RecognizedBook) : RegisterBookRequestBody {
            return RegisterBookRequestBody(
                title = recognizedBook.title,
                content = recognizedBook.content,
                authors = recognizedBook.authors,
                publisher = recognizedBook.publisher,
                translators = recognizedBook.translators,
                thumbnail_url = recognizedBook.thumbnail_url,
                isbn = recognizedBook.isbn,
                total_page = recognizedBook.total_page,
                meta = recognizedBook.meta
            )
        }
    }
}