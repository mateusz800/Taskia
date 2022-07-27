package com.mabn.taskia.ui.taskForm

import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task

data class FormState(
    val title: String = "",
    val dayLabel: String = "",
    val timeLabel: String = "",
    val subtasks: List<Task> = listOf(),
    val tags: List<Tag> = listOf(Tag(value = ""))
) {
    fun validateData(): Boolean {
        return title.isNotBlank()
    }
}
