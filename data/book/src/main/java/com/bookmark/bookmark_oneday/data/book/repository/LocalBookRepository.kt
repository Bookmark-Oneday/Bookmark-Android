package com.bookmark.bookmark_oneday.data.book.repository

import android.annotation.SuppressLint
import com.bookmark.bookmark_oneday.core.model.BaseResponse
import com.bookmark.bookmark_oneday.core.model.PagingData
import com.bookmark.bookmark_oneday.core.model.toTimeString
import com.bookmark.bookmark_oneday.core.room.dao.BookDao
import com.bookmark.bookmark_oneday.core.room.entity.BookEntity
import com.bookmark.bookmark_oneday.core.room.entity.ReadingHistoryEntity
import com.bookmark.bookmark_oneday.core.room.entity.RegisteredBookEntity
import com.bookmark.bookmark_oneday.domain.book.model.BookDetail
import com.bookmark.bookmark_oneday.domain.book.model.BookItem
import com.bookmark.bookmark_oneday.domain.book.model.MyLibraryItem
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistory
import com.bookmark.bookmark_oneday.domain.book.model.ReadingHistoryWithBook
import com.bookmark.bookmark_oneday.domain.book.model.ReadingInfo
import com.bookmark.bookmark_oneday.domain.book.model.RecognizedBook
import com.bookmark.bookmark_oneday.domain.book.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.coroutines.CoroutineContext

class LocalBookRepository constructor(
    private val bookDao: BookDao,
    private val defaultDispatcher: CoroutineContext = Dispatchers.IO + SupervisorJob()
) : BookRepository {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @SuppressLint("SimpleDateFormat")
    private val dateOnlyFormatter = SimpleDateFormat("yyyy-MM-dd")

    override suspend fun getBookList(
        perPage: Int,
        key: String,
        sortType: String
    ): BaseResponse<PagingData<BookItem>> {
        val pageIdx = key.toIntOrNull() ?: throw IllegalArgumentException("page Key must be int : $key")
        val bookList = bookDao.getBookItemList(pageSize = perPage, pageIdx = pageIdx)
        val amountOfBook = bookDao.getAmountOfRegisteredBook()

        return BaseResponse.Success(data = PagingData(
            dataList = bookList.map { bookItemDto ->
                BookItem(
                    id = bookItemDto.book_id,
                    thumbnail = bookItemDto.titleImage,
                    title = bookItemDto.title,
                    author = bookItemDto.authors.joinToString(","),
                    reading = bookItemDto.reading,
                    favorite = bookItemDto.favorite
                )
            },
            nextPageToken = (pageIdx + 1).toString(),
            totalItemCount = amountOfBook,
            isFinish = bookList.isEmpty()
        ))
    }

    override suspend fun getMyLibraryList(
        perPage: Int,
        key: String,
        sortType: String
    ): BaseResponse<PagingData<MyLibraryItem.Book>> = withContext(defaultDispatcher) {
        val pageIdx = key.toIntOrNull() ?: throw IllegalArgumentException("page Key must be int : $key")
        try {
            val bookList = bookDao.getBookItemList(pageSize = perPage, pageIdx = pageIdx)
            val amountOfBook = bookDao.getAmountOfRegisteredBook()

            return@withContext BaseResponse.Success(data = PagingData(
                dataList = bookList.map { bookItemDto ->
                    MyLibraryItem.Book(
                        id = bookItemDto.book_id,
                        thumbnail = bookItemDto.titleImage,
                        title = bookItemDto.title,
                        author = bookItemDto.authors.joinToString(","),
                        reading = bookItemDto.reading,
                        favorite = bookItemDto.favorite
                    )
                },
                nextPageToken = (pageIdx + 1).toString(),
                totalItemCount = amountOfBook,
                isFinish = bookList.isEmpty()
            ))

        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun getBookDetail(bookId: String): BaseResponse<BookDetail> = withContext(defaultDispatcher) {
        try {
            val registeredBook = bookDao.getRegisteredBook(bookId.toInt())[0]
            val bookInfo = bookDao.getBook(registeredBook.isbn)[0]
            val readingHistory = bookDao.getReadingHistoryList(bookId.toInt()).map { historyDto ->
                ReadingHistory(
                    id = historyDto.id,
                    dateString = historyDto.date.toTimeString(),
                    time = historyDto.time
                )
            }

            return@withContext BaseResponse.Success(
                data = BookDetail(
                    bookId = bookId,
                    title = bookInfo.title,
                    author = bookInfo.authors.joinToString(","),
                    translators = bookInfo.translators.joinToString(","),
                    publisher = bookInfo.publisher,
                    imageUrl = bookInfo.backgroundUri,
                    totalPage = registeredBook.totalPage,
                    currentPage = registeredBook.currentPage,
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

    override suspend fun removeBook(bookId: String): BaseResponse<Nothing> = withContext(defaultDispatcher) {
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

    override suspend fun removeAllBook(): BaseResponse<Nothing> = withContext(defaultDispatcher) {
        try {
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
            bookDao.updatePageInfo(bookId = bookId.toInt(), currentPage = currentPage, totalPage = totalPage)
            return@withContext BaseResponse.EmptySuccess
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun deleteHistoryItem(
        bookId: String,
        targetId: String
    ): BaseResponse<ReadingInfo> = withContext(defaultDispatcher) {
        requireNotNull(targetId.toIntOrNull()) { "readingHistoryId must be int : $targetId" }
        requireNotNull(bookId.toIntOrNull()) {"bookId must be int : $bookId"}
        try {
            bookDao.deleteReadingHistory(targetId.toInt())
            val readingInfo = readingInfo(bookId.toInt())
            return@withContext BaseResponse.Success(data = readingInfo)
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun deleteHistoryAll(bookId: String): BaseResponse<ReadingInfo> = withContext(defaultDispatcher) {
        requireNotNull(bookId.toIntOrNull()) {"bookId must be int : $bookId"}
        try {
            bookDao.deleteAllReadingHistoryOfBook(bookId.toInt())
            val readingInfo = readingInfo(bookId.toInt())
            return@withContext BaseResponse.Success(data = readingInfo)
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun getReadingInfo(bookId: String): BaseResponse<ReadingInfo> = withContext(defaultDispatcher) {
        try {
            val readingInfo = readingInfo(bookId.toInt())
            return@withContext BaseResponse.Success(data = readingInfo)
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    private suspend fun readingInfo(bookId : Int) : ReadingInfo {
        val readingHistoryList = getReadingHistory(bookId)

        val currentTime = Calendar.getInstance().time
        val currentReadingTime = getDailyTotalTime(currentTime)
        // todo : user 데이터에 접근해 목표 시간 가져오기
        val dailyGoalTime = 0

        return ReadingInfo(
            dailyGoalTime = dailyGoalTime,
            dailyReadingTime = currentReadingTime,
            readingHistoryList = readingHistoryList
        )
    }

    private suspend fun getDailyTotalTime(currentTime: Date): Int {
        return bookDao.getDailyTotalReadingTime(dateQuery = dateOnlyFormatter.format(currentTime))
    }

    private suspend fun getReadingHistory(bookId : Int) : List<ReadingHistory> {
        val historyList = bookDao.getReadingHistoryList(bookId = bookId)
        return historyList.map { historyDto ->
            ReadingHistory(
                id = historyDto.id,
                dateString = historyDto.date.toTimeString(),
                time = historyDto.time
            )
        }
    }

    override suspend fun updateHistory(bookId: String, time: Int): BaseResponse<ReadingInfo> = withContext(defaultDispatcher) {
        requireNotNull(bookId.toIntOrNull()) {"bookId must be int : $bookId"}
        try {
            val currentTime = Calendar.getInstance().time
            bookDao.insertReadingHistory(
                ReadingHistoryEntity(
                    bookId = bookId.toInt(),
                    userId = 0,
                    date = formatter.format(currentTime),
                    timeSec = time
                )
            )
            val readingInfo = readingInfo(bookId.toInt())
            return@withContext BaseResponse.Success(data = readingInfo)
        } catch (e: Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun registerBook(book: RecognizedBook): BaseResponse<Nothing> = withContext(defaultDispatcher) {
        try {
            val bookExist = bookDao.getBookCount(book.isbn) > 0
            if (bookExist) {
                return@withContext BaseResponse.Failure(
                    errorMessage = "이미 등록된 책입니다.",
                    errorCode = 409
                )
            } else {
                val bookEntity = BookEntity(
                    isbn = book.isbn,
                    title = book.title,
                    authors = book.authors,
                    translators = book.translators,
                    publisher = book.publisher,
                    introduce = book.content,
                    backgroundUri = book.thumbnail_url
                )
                bookDao.insertBook(bookEntity)
            }

            val registeredBook = RegisteredBookEntity(
                isbn = book.isbn,
                totalPage = book.total_page,
                currentPage = 0,
                registeredAt = formatter.format(Calendar.getInstance().time)
            )
            bookDao.insertRegisteredBook(registeredBook)
            return@withContext BaseResponse.EmptySuccess
        } catch (e : Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun checkDuplicate(isbn: String): BaseResponse<Nothing> = withContext(defaultDispatcher) {
        try {
            // todo : 중복 화인 절차에 따라 변경하기
            delay(1000L)
            return@withContext BaseResponse.Failure(errorCode = 409, errorMessage = "exist book isbn")
        } catch (e : Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun getReadingHistoryOfMonth(
        year: Int,
        month: Int
    ): BaseResponse<List<ReadingHistory>> = withContext(defaultDispatcher) {
        try {
            val query = String.format("%d-%02d", year, month)
            val response = bookDao.getReadingHistoryByDateQuery(query)
            return@withContext BaseResponse.Success(
                data = response.map { historyDto ->
                    ReadingHistory(
                        id = historyDto.id,
                        dateString = historyDto.date.toTimeString(),
                        time = historyDto.time
                    )
                }
            )
        } catch (e : Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

    override suspend fun getReadingHistoryWithBookOfDay(
        year: Int,
        month: Int,
        day: Int
    ): BaseResponse<List<ReadingHistoryWithBook>> = withContext(defaultDispatcher) {
        try {
            val query = String.format("%d-%02d-%02d", year, month, day)
            val response = bookDao.getReadingHistoryWithBookByDateQuery(query)
            return@withContext BaseResponse.Success(
                data = response.map { historyWithBookDto ->
                    ReadingHistoryWithBook(
                        dateString = historyWithBookDto.date.toTimeString(),
                        time = historyWithBookDto.time,
                        bookTitle = historyWithBookDto.title,
                        author = historyWithBookDto.authors.joinToString(","),
                        bookCover = historyWithBookDto.titleImage,
                        bookId = historyWithBookDto.bookId.toString()
                    )
                }
            )
        } catch (e : Exception) {
            return@withContext BaseResponse.Failure(
                errorCode = -1,
                errorMessage = "${e.message}"
            )
        }
    }

}