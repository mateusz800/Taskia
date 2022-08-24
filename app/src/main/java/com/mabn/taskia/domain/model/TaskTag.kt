package com.mabn.taskia.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["taskId", "tagId"]
)
data class TaskTag(
    val taskId: Long,
    @ColumnInfo(index = true)
    val tagId: Long
)