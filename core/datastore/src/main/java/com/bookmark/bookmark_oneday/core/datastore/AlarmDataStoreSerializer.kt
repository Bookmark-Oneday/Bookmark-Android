package com.bookmark.bookmark_oneday.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AlarmDataStoreSerializer : Serializer<Alarm> {
    override val defaultValue: Alarm
        get() = Alarm.newBuilder().setAlarmOn(false).setHour(20).setMinute(0).build()

    override suspend fun readFrom(input: InputStream): Alarm {
        try {
            return Alarm.parseFrom(input)
        } catch (exception : InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Alarm, output: OutputStream) {
        t.writeTo(output)
    }
}