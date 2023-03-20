package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_info_datasource.BookInfoDataSource
import com.bookmark.bookmark_oneday.data.datasource.book_info_datasource.KakaoBookInfoDataSource
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.repository.SearchBookDetailRepository

class SearchBookDetailRepositoryImpl constructor(
    private val dataSource: BookInfoDataSource = KakaoBookInfoDataSource()
) : SearchBookDetailRepository {
    override suspend fun searchAndGetBookDetail(isbn: String): BaseResponse<RecognizedBook> {
        val response = dataSource.getBookInfo(isbn = isbn)
        return if (response.isSuccessful) {
            val data = response.body()!!.documents[0]

            val recognizedBook = RecognizedBook(
                title = data.title,
                content = data.contents,
                authors = data.authors,
                translators = data.translators,
                thumbnail_url = data.thumbnail,
                isbn = data.isbn,
                total_page = 0,
                meta = response.raw().toString()
            )

            BaseResponse.Success(data = recognizedBook)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }
}