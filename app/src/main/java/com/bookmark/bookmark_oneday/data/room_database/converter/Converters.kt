package com.bookmark.bookmark_oneday.data.room_database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(value : List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value : String) = Gson().fromJson(value, Array<String>::class.java).toList()
}