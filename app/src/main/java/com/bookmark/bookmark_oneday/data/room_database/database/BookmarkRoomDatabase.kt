package com.bookmark.bookmark_oneday.data.room_database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bookmark.bookmark_oneday.data.room_database.converter.Converters
import com.bookmark.bookmark_oneday.data.room_database.dao.BookDao
import com.bookmark.bookmark_oneday.data.room_database.dao.OneLineDao
import com.bookmark.bookmark_oneday.data.room_database.entity.Book
import com.bookmark.bookmark_oneday.data.room_database.entity.OneLine
import com.bookmark.bookmark_oneday.data.room_database.entity.ReadingHistory
import com.bookmark.bookmark_oneday.data.room_database.entity.RegisteredBook

@Database(entities = [Book::class, RegisteredBook::class, OneLine::class, ReadingHistory::class], version = 1)
@TypeConverters(Converters::class)
abstract class BookmarkRoomDatabase : RoomDatabase() {
    abstract fun bookDao() : BookDao
    abstract fun oneLineDao() : OneLineDao

    companion object {
        private lateinit var instance : BookmarkRoomDatabase

        fun getInstance(context : Context) : BookmarkRoomDatabase {
            if (!::instance.isInitialized) {
                synchronized(BookmarkRoomDatabase::class) {
                    instance = Room.databaseBuilder(context, BookmarkRoomDatabase::class.java, "bookMark-room")
                        .fallbackToDestructiveMigration().build()
                }
            }

            return instance
        }
    }
}