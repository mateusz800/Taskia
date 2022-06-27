package com.mabn.taskia.domain.model

import android.content.Context
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mabn.taskia.domain.util.dbConverter.LocalDateTimeConverter
import java.time.LocalDateTime

@Entity(
    indices = [Index(
        value = ["googleId"],
        unique = true
    )]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var title: String,
    var status: Boolean = false,
    var description: String? = null,
    var parentId: Long? = null,
    var order: Int? = id.toInt(),
    var endDate: LocalDateTime? = null,
    var completionTime: LocalDateTime? = null,

    // External providers ids
    val googleId: String? = null
) {
    fun getEndDay(context: Context): String {
        if (endDate != null) {
            return LocalDateTimeConverter.dateToString(endDate!!, context)
        }
        return ""
    }
}