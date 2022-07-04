package com.mabn.taskia.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TaskAndSubtasks(
    @Embedded var task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    ) var subtasks: List<Task>,
    @Relation(
        parentColumn = "id",
        entity = Tag::class,
        entityColumn = "id",
        associateBy = Junction(
            value = TaskTag::class,
            parentColumn = "taskId",
            entityColumn = "tagId"
        )
    )
    val tags: List<Tag>
)