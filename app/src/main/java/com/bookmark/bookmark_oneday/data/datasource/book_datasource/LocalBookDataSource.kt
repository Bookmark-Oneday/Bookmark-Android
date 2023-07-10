package com.bookmark.bookmark_oneday.data.datasource.book_datasource

import android.annotation.SuppressLint
import com.bookmark.bookmark_oneday.data.models.dto.BookDetailDto
import com.bookmark.bookmark_oneday.data.models.dto.BookItemDto
import com.bookmark.bookmark_oneday.data.models.dto.BookTimerDto
import com.bookmark.bookmark_oneday.data.room_database.dao.BookDao
import com.bookmark.bookmark_oneday.data.room_database.entity.Book
import com.bookmark.bookmark_oneday.data.room_database.entity.ReadingHistory
import com.bookmark.bookmark_oneday.data.room_database.entity.RegisteredBook
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocalBookDataSource @Inject constructor(
    private val bookDao: BookDao,
) : BookDataSource {
    private val defaultDispatcher: CoroutineContext = Dispatchers.IO + SupervisorJob()

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @SuppressLint("SimpleDateFormat")
    private val dateOnlyFormatter = SimpleDateFormat("yyyy-MM-dd")

    // todo 중복 여부 무시 인자 필요
    override suspend fun registerBook(recognizedBook: RecognizedBook): BaseResponse<Nothing> =
        withContext(defaultDispatcher) {
            try {
                val bookExist = bookDao.getBookCount(recognizedBook.isbn) > 0
                if (bookExist) {
                    return@withContext BaseResponse.Failure(
                        errorMessage = "이미 등록된 책입니다.",
                        errorCode = 409
                    )
                } else {
                    val bookEntity = Book(
                        isbn = recognizedBook.isbn,
                        title = recognizedBook.title,
                        authors = recognizedBook.authors,
                        translators = recognizedBook.translators,
                        publisher = recognizedBook.publisher,
                        introduce = recognizedBook.content,
                        backgroundUri = recognizedBook.thumbnail_url
                    )
                    bookDao.insertBook(bookEntity)
                }

                val registeredBook = RegisteredBook(
                    isbn = recognizedBook.isbn,
                    totalPage = recognizedBook.total_page,
                    currentPage = 0,
                    registeredAt = formatter.format(Calendar.getInstance().time)
                )
                bookDao.insertRegisteredBook(registeredBook)
                return@withContext BaseResponse.EmptySuccess
            } catch (e: Exception) {
                return@withContext BaseResponse.Failure(
                    errorCode = -1,
                    errorMessage = "${e.message}"
                )
            }
        }

    override suspend fun getBookDetail(bookId: String): BaseResponse<BookDetailDto> =
        withContext(defaultDispatcher) {
            try {
                val registeredBook = bookDao.getRegisteredBook(bookId.toInt())[0]
                val bookInfo = bookDao.getBook(registeredBook.isbn)[0]
                val readingHistory = bookDao.getReadingHistoryList(bookId.toInt())

                return@withContext BaseResponse.Success(
                    data = BookDetailDto(
                        book_id = bookId,
                        title = bookInfo.title,
                        authors = bookInfo.authors,
                        translators = bookInfo.translators,
                        publisher = bookInfo.publisher,
                        titleImage = bookInfo.backgroundUri,
                        total_page = registeredBook.totalPage,
                        current_page = registeredBook.currentPage,
                        history = readingHistory
                    )
                )
            } catch (e: Exception) {
                return@withContext BaseResponse.Failure(
                    errorCode = -1,
                    errorMessage = "${e.message}"
                )
            }
        }

    override suspend fun getBookList(
        perPage: Int,
        continuousToken: String,
        sortType: String
    ): BaseResponse<PagingData<BookItemDto>> = withContext(defaultDispatcher) {
        try {
            val bookItemList = bookDao.getBookItemList(continuousToken.toInt(), perPage)
            return@withContext BaseResponse.Success(
                PagingData(
                    dataList = bookItemList,
                    nextPageToken = continuousToken + 1,
                    totalItemCount = 0
                )
            )
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun deleteBook(bookId: String): BaseResponse<Nothing> =
        withContext(defaultDispatcher) {
            try {
                bookDao.deleteRegisteredBook(bookId = bookId.toInt())
                return@withContext BaseResponse.EmptySuccess
            } catch (e: Exception) {
                return@withContext BaseResponse.Failure(
                    errorCode = -1,
                    errorMessage = "${e.message}"
                )
            }
        }

    override suspend fun updateReadingPage(
        bookId: String,
        currentPage: Int,
        totalPage: Int
    ): BaseResponse<Nothing> = withContext(defaultDispatcher) {
        try {
            bookDao.updatePageInfo(bookId.toInt(), currentPage, totalPage)
            return@withContext BaseResponse.EmptySuccess
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(errorCode = -1, errorMessage = "${e.message}")
        }
    }

    override suspend fun getBookTimer(bookId: String): BaseResponse<BookTimerDto> =
        withContext(defaultDispatcher) {
            try {
                val currentTime = Calendar.getInstance().time
                val dailyTotalTime = getDailyTotalTime(currentTime)

                val historyList = bookDao.getReadingHistoryList(bookId.toInt())
                val timeDto = BookTimerDto(
                    user_id = "",
                    target_time = 0,
                    daily = dailyTotalTime,
                    book = BookTimerDto.BookTimerBookDto(
                        book_id = bookId,
                        history = historyList
                    )
                )
                return@withContext BaseResponse.Success(data = timeDto)
            } catch (e: Exception) {
                return@withContext BaseResponse.Failure(
                    errorCode = -1,
                    errorMessage = "${e.message}"
                )
            }
        }

    // todo 목표 시간 반영 필요
    override suspend fun updateReadingHistory(
        bookId: String,
        readingTime: Int
    ): BaseResponse<BookTimerDto> = withContext(defaultDispatcher) {
        try {
            val currentTime = Calendar.getInstance().time

            bookDao.insertReadingHistory(
                ReadingHistory(
                    bookId = bookId.toInt(),
                    userId = 0,
                    date = formatter.format(currentTime),
                    timeSec = readingTime
                )
            )

            val dailyTotalTime = getDailyTotalTime(currentTime)

            val historyList = bookDao.getReadingHistoryList(bookId.toInt())
            val timeDto = BookTimerDto(
                user_id = "",
                target_time = 0,
                daily = dailyTotalTime,
                book = BookTimerDto.BookTimerBookDto(
                    book_id = bookId,
                    history = historyList
                )
            )
            return@withContext BaseResponse.Success(data = timeDto)
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(errorCode = -1, errorMessage = "${e.message}")
        }
    }

    override suspend fun deleteReadingHistory(
        bookId: String,
        historyId: String?
    ): BaseResponse<BookTimerDto> = withContext(defaultDispatcher) {
        try {
            val removeAll = historyId == null

            if (removeAll) {
                bookDao.deleteAllReadingHistoryOfBook(bookId = bookId.toInt())
            } else {
                bookDao.deleteReadingHistory(historyId = historyId!!.toInt())
            }

            val dailyTotalTime = getDailyTotalTime(Calendar.getInstance().time)

            val historyList = bookDao.getReadingHistoryList(bookId.toInt())
            val timeDto = BookTimerDto(
                user_id = "",
                target_time = 0,
                daily = dailyTotalTime,
                book = BookTimerDto.BookTimerBookDto(
                    book_id = bookId,
                    history = historyList
                )
            )
            return@withContext BaseResponse.Success(data = timeDto)
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(errorCode = -1, errorMessage = "${e.message}")
        }
    }

    private suspend fun getDailyTotalTime(currentTime: Date): Int {
        return bookDao.getDailyTotalReadingTime(dateQuery = dateOnlyFormatter.format(currentTime))
    }
}