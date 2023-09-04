package com.bookmark.bookmark_oneday.core.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bookmark.bookmark_oneday.core.room.converter.Converters
import com.bookmark.bookmark_oneday.core.room.dao.BookDao
import com.bookmark.bookmark_oneday.core.room.dao.OneLineDao
import com.bookmark.bookmark_oneday.core.room.entity.BookEntity
import com.bookmark.bookmark_oneday.core.room.entity.OneLineEntity
import com.bookmark.bookmark_oneday.core.room.entity.ReadingHistoryEntity
import com.bookmark.bookmark_oneday.core.room.entity.RegisteredBookEntity

@Database(
    entities = [BookEntity::class, RegisteredBookEntity::class, OneLineEntity::class, ReadingHistoryEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class BookmarkRoomDatabase : RoomDatabase() {
    abstract fun bookDao() : BookDao
    abstract fun oneLineDao() : OneLineDao

    companion object {
        private lateinit var instance : BookmarkRoomDatabase

        fun getInstance(context : Context) : BookmarkRoomDatabase {
            if (!Companion::instance.isInitialized) {
                synchronized(BookmarkRoomDatabase::class) {
                    instance = Room.databaseBuilder(context, BookmarkRoomDatabase::class.java, "bookMark-local").build()
                }
            }

            return instance
        }

        fun clearDatabase() {
            if (!Companion::instance.isInitialized) return
            instance.clearAllTables()
        }
    }
}