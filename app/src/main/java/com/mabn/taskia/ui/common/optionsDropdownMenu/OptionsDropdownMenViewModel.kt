package com.mabn.taskia.ui.common.optionsDropdownMenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.domain.model.TaskAndSubtasks
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsDropdownMenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _deleteAllTaskButtonAvailable = MutableLiveData(true)
    val deleteAllTaskButtonAvailable: LiveData<Boolean>
        get() = _deleteAllTaskButtonAvailable

    private val _deleteCompleteTaskButtonAvailable = MutableLiveData(true)
    val deleteCompleteTaskButtonAvailable: LiveData<Boolean>
        get() = _deleteCompleteTaskButtonAvailable

    private val _allTasks = mutableListOf<TaskAndSubtasks>()
    private var _queuedTask: (() -> Unit)? = null

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAll().collect {
                _allTasks.clear()
                if (it.isNotEmpty()) {
                    _allTasks.addAll(it)
                    _deleteAllTaskButtonAvailable.postValue(true)
                } else {
                    _deleteAllTaskButtonAvailable.postValue(false)
                }
                checkCompletedTask()
            }
        }
    }

    private fun checkCompletedTask() {
        _deleteCompleteTaskButtonAvailable.postValue(_allTasks.any { it.task.status })
    }


    fun removeAllCompletedTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val completedTasks =
                _allTasks
                    .filter { it.task.status }
                    .flatMap { listOf(it.task, *it.subtasks.toTypedArray()) }
            taskRepository.deleteAll(*completedTasks.toTypedArray())
        }
    }

    fun removeAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val allTasks = _allTasks
                .flatMap { listOf(it.task, *it.subtasks.toTypedArray()) }
            taskRepository.deleteAll(*allTasks.toTypedArray())

        }
    }

    fun queueTask(task: () -> Unit) {
        _queuedTask = task
    }

    fun execQueuedTask() {
        _queuedTask?.invoke()
    }
}