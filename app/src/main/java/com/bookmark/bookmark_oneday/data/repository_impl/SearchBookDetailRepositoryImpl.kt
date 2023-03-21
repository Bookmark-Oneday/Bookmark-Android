package com.bookmark.bookmark_oneday.data.repository_impl

import com.bookmark.bookmark_oneday.data.datasource.book_info_datasource.BookInfoDataSource
import com.bookmark.bookmark_oneday.data.datasource.book_info_datasource.KakaoBookInfoDataSource
import com.bookmark.bookmark_oneday.domain.model.BaseResponse
import com.bookmark.bookmark_oneday.domain.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.repository.SearchBookDetailRepository
import java.text.SimpleDateFormat
import java.util.*

class SearchBookDetailRepositoryImpl constructor(
    private val dataSource: BookInfoDataSource = KakaoBookInfoDataSource()
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
                isbn = data.isbn,
                total_page = 0,
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