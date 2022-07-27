package com.mabn.taskia.ui.taskForm

import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task

sealed class FormEvent {
    data class TitleChanged(val newTitle:String): FormEvent()
    data class TaskDateChanged(val date: String) : FormEvent()
    data class TaskTimeChanged(val time: String) : FormEvent()
    data class SubtaskTitleChanged(val subtask: Task, val newTitle: String) : FormEvent()
    object AddNewSubtask : FormEvent()
    data class AddNewTag(val forceFocus: Boolean) : FormEvent()
    data class TagValueChanged(val tag: Tag, val newValue: String) : FormEvent()
    object Submit : FormEvent()

}
