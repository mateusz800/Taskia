package com.example.doit.ui.common.optionsDropdownMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsDropdownMenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var queuedTask: (() -> Unit)? = null


    fun removeAllCompletedTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val completedTasks =
                taskRepository.getAll().first()
                    .filter { it.task.status }
                    .flatMap { listOf(it.task, *it.subtasks.toTypedArray()) }
            taskRepository.deleteAll(*completedTasks.toTypedArray())
        }
    }

    fun removeAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val allTasks = taskRepository.getAll().first()
                .flatMap { listOf(it.task, *it.subtasks.toTypedArray()) }
            taskRepository.deleteAll(*allTasks.toTypedArray())

        }
    }

    fun queueTask(task: () -> Unit) {
        queuedTask = task
    }

    fun execQueuedTask() {
        queuedTask?.invoke()
    }
}