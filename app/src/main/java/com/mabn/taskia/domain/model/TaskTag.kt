package com.mabn.taskia.domain.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["taskId", "tagId"]
)
data class TaskTag(
    val taskId: Long,
    val tagId: Long
)