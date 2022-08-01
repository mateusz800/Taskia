package com.mabn.taskia.ui.taskList

import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task

sealed class ListEvent {
    data class TaskRemoved(val task: Task) : ListEvent()
    data class TaskStatusChanged(val task: Task) : ListEvent()
    data class FilterTagsChanged(val tags: List<Tag>) : ListEvent()


}
