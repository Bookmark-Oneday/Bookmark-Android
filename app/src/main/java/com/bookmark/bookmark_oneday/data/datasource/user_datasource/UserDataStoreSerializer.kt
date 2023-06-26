package com.bookmark.bookmark_oneday.data.datasource.user_datasource

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.bookmark.bookmark_oneday.User
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserDataStoreSerializer : Serializer<User> {
    override val defaultValue: User
        get() = User.newBuilder().setNickname("닉네임").setProfileUri("").setBio("loading...").build()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return User.parseFrom(input)
        } catch (exception : InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        t.writeTo(output)
    }
}