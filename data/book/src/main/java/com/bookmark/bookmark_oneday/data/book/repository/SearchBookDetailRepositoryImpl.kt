package com.bookmark.bookmark_oneday.data.book.repository

import com.bookmark.bookmark_oneday.data.book.datasorce.BookInfoDataSource
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.book.repository.SearchBookDetailRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SearchBookDetailRepositoryImpl @Inject constructor(
    private val dataSource: BookInfoDataSource
) : SearchBookDetailRepository {
    override suspend fun searchAndGetBookDetail(isbn: String): BaseResponse<RecognizedBook> {
        val response = dataSource.getBookInfo(isbn = isbn)
        return if (response.isSuccessful) {
            if (response.body()!!.documents.isEmpty()){
                return BaseResponse.Failure(204, "cannot find resource")
            }

            val data = response.body()!!.documents[0]

            val recognizedBook = RecognizedBook(
                title = data.title,
                content = data.contents,
                authors = data.authors,
                translators = data.translators,
                thumbnail_url = data.thumbnail,
                isbn = data.getSingleIsbn(),
                total_page = 100,
                meta = response.raw().toString(),
                publisher = data.publisher,
                dateTime = mapDateString(data.datetime)
            )

            BaseResponse.Success(data = recognizedBook)
        } else {
            val errorMessage = response.message()
            val errorCode = response.code()
            BaseResponse.Failure(errorCode, errorMessage)
        }
    }

    private fun mapDateString(dateString : String) : String {
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.000"
        val outputPattern = "yyyy.MM.dd"

        return try {
            val date = SimpleDateFormat(inputPattern, Locale.KOREA).parse(dateString)
            val str = SimpleDateFormat(outputPattern, Locale.KOREA).format(date!!)
            str
        } catch (e : Exception) {
            dateString
        }

    }
}