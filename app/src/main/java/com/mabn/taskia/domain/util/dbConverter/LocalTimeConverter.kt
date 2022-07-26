package com.mabn.taskia.domain.util.dbConverter

import androidx.room.TypeConverter
import java.time.LocalTime

object LocalTimeConverter {
    @TypeConverter
    fun fromLong(value: Long?): LocalTime? {
        return value?.let { LocalTime.ofNanoOfDay(it) }
    }

    @TypeConverter
    fun timeToLong(time: LocalTime?): Long? {
        return time?.toNanoOfDay()
    }
}