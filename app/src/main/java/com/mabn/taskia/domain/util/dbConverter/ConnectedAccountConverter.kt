package com.mabn.taskia.domain.util.dbConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mabn.taskia.domain.model.ConnectedAccount

object ConnectedAccountConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): ConnectedAccount? {
        return gson.fromJson(value, ConnectedAccount::class.java)
    }

    @TypeConverter
    fun fromConnectedAccount(value: ConnectedAccount?): String? {
        return gson.toJson(value)
    }
}