package com.example.doit.ui.taskForm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException
import javax.inject.Inject
import kotlin.streams.toList

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    var isVisible = MutableStateFlow(false)
    private var _task: Task? = null
    private val _title = MutableStateFlow("")
    val title: StateFlow<String>
        get() = _title

    val subtasks = SnapshotStateList<Task>()

    fun onTitleChanged(title: String) {
        _title.value = title
    }

    fun verifyData(): Boolean {
        if (_title.value.isNullOrEmpty()) {
            return false
        }
        return true
    }

    fun saveTask() {
        val task: Task = if (_task == null) {
            Task(title = title.value)
        } else {
            _task!!.copy(title = title.value)
        }
        val subtaskList = subtasks.parallelStream()
            .filter { it.title.isNotEmpty() }
            .toList()
        viewModelScope.launch(Dispatchers.IO) {
            val parentId =
                if (task.id == 0L) {
                    taskRepository.insertAll(task)[0]
                } else {
                    task.id
                }
            subtaskList.forEach {
                val subtask = it
                subtask.parentId = parentId
                if (subtask.id == 0L) {
                    task.status = false
                    taskRepository.insertAll(subtask)
                } else {
                    taskRepository.update(subtask)
                }
            }
            taskRepository.update(task)

        }
        clear()
    }

    fun setTask(task: Task) {
        _task = task
        _title.value = task.title
        viewModelScope.launch(Dispatchers.IO) {
            subtasks.clear()
            val temp = taskRepository.getSubtasks(task)
            subtasks.addAll(temp)
        }

    }

    fun addNewSubtask() {
        if (subtasks.isEmpty() || subtasks.last().title.isNotBlank()) {
            subtasks.add(Task(title = ""))
        }
    }

    fun updateSubtaskTitle(subtask: Task, newTitle: String) {
        try {
            subtasks[subtasks.indexOf(subtask)] =
                subtasks[subtasks.indexOf(subtask)].copy(title = newTitle)
        } catch (e:IndexOutOfBoundsException){
            e.printStackTrace()
        }
    }

    fun clear() {
        _task = null
        _title.value = ""
        subtasks.clear()
    }
}