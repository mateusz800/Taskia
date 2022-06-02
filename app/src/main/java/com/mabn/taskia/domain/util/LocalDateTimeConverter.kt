package com.mabn.taskia.domain.util

import android.content.Context
import androidx.room.TypeConverter
import com.mabn.taskia.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

    fun dateToString(date: LocalDateTime, context: Context): String {
        return when (date.dayOfYear) {
            LocalDateTime.now().dayOfYear - 1 -> {
                context.getString(R.string.yesterday)
            }
            LocalDateTime.now().dayOfYear -> {
                context.getString(R.string.today)
            }
            LocalDateTime.now().dayOfYear + 1 -> {
                context.getString(R.string.tomorrow)
            }
            else -> {
                "%02d".format(date.dayOfMonth) + " " + date.month.toString()
            }
        }

    }
}