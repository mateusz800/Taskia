package com.mabn.taskia.domain.util.dbConverter

import android.content.Context
import androidx.room.TypeConverter
import com.mabn.taskia.R
import java.time.*

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

    fun stringToDate(value: String, context: Context): LocalDateTime? {
        return when (value) {
            context.getString(R.string.yesterday) -> LocalDate.now().minusDays(1)
            context.getString(R.string.today) -> LocalDate.now()
            context.getString(R.string.tomorrow) -> LocalDate.now().plusDays(1)
            context.getString(R.string.no_deadline) -> null
            else -> {
                val day = value.split(" ")[0].toInt()
                val month = value.split(" ")[1]
                LocalDate.of(LocalDate.now().year, Month.valueOf(month), day)
            }
        }?.atStartOfDay()
    }
}