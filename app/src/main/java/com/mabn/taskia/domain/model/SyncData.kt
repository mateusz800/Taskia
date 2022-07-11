package com.mabn.taskia.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class SyncData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long,
    val operation: SyncDataOperation
)

enum class SyncDataOperation {
    UPDATE, DELETE, INSERT
}

data class SyncDataAndTask(
    @Embedded var task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "Id"
    ) var subtasks: List<Task>
)