package com.example.doit.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class TaskAndSubtasks(
    @Embedded var task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    ) var subtasks: List<Task>
)