package com.mabn.taskia.domain.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mabn.taskia.R
import java.time.LocalDateTime

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var title: String,
    var status: Boolean = false,
    var description: String? = null,
    var parentId: Long? = null,
    var order: Int? = id.toInt(),
    var endDate: LocalDateTime? = null
) {
    fun getEndDay(context: Context): String {
        if (endDate != null) {
            if (endDate!!.dayOfYear == LocalDateTime.now().dayOfYear) {
                return context.getString(R.string.today)
            } else if (endDate!!.dayOfYear == LocalDateTime.now().dayOfYear + 1) {
                return context.getString(R.string.tomorrow)
            }
            return "%02d".format(endDate!!.dayOfMonth) + " " + endDate!!.month.toString()
        }
        return ""
    }
}