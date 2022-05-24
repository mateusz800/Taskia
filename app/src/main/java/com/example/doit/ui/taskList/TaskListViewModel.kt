package com.example.doit.ui.taskList

import android.content.res.Resources
import android.provider.Settings.Global.getString
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.R
import com.example.doit.domain.model.Message
import com.example.doit.domain.model.MessageType
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.repository.MessageRepository
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<SnapshotStateMap<Task, List<Task>>>()
    val tasks: LiveData<SnapshotStateMap<Task, List<Task>>>
        get() = _tasks
    private var recentlyRemovedTask: Task? = null

    init {
        collectTasks()
    }

    fun removeTask(task: Task) {
        recentlyRemovedTask = task
        viewModelScope.launch(Dispatchers.IO) {
            val parentTaskId = task.parentId
            if (taskRepository.remove(task)) {
                messageRepository.insertMessage(
                    Message(
                        text = Resources.getSystem().getString(R.string.task_removed),
                        actionText = Resources.getSystem().getString(R.string.undo),
                        actionFun = { restoreRemovedTask() },
                        type = MessageType.SNACKBAR
                    )
                )
            }
            if (parentTaskId != null) {
                val parentTask = taskRepository.getById(parentTaskId)
                if (parentTask != null) {
                    checkIfCompleted(parentTask)
                }
            }

        }
    }

    fun toggleTaskStatus(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val subtasksList = _tasks.value?.get(task)
            var newStatus = task.status
            if (subtasksList.isNullOrEmpty()) {
                newStatus = !task.status
            } else {
                val allSubtasksCompleted = subtasksList.stream().allMatch { task -> task.status }
                if (allSubtasksCompleted || task.status) {
                    newStatus = !task.status;
                } else {
                    messageRepository.insertMessage(
                        Message(text = Resources.getSystem().getString(R.string.uncompleted_tasks))
                    )
                }
            }

            if (newStatus != task.status) {
                task.status = newStatus
                taskRepository.update(task)
                if (task.parentId != null) {
                    val parentTask =
                        _tasks.value!!
                            .filter { (key, _) -> key.id == task.parentId }
                            .keys
                            .first()
                    if (!newStatus) {
                        parentTask.status = false
                        taskRepository.update(parentTask)
                    } else {
                        checkIfCompleted(parentTask)
                    }

                }
            }
        }
    }

    private fun checkIfCompleted(task: Task) {
        val allSubtasks = taskRepository.getSubtasks(task)
        task.status = allSubtasks.stream().allMatch { t -> t.status }
        taskRepository.update(task)
    }

    private fun restoreRemovedTask() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyRemovedTask != null) {
                taskRepository.insertAll(recentlyRemovedTask!!)
            }
        }
    }


    private fun collectTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAll().collect { result ->
                val newTasksData = SnapshotStateMap<Task, List<Task>>()
                result.forEach {
                    newTasksData[it.task] = it.subtasks
                }
                _tasks.postValue(newTasksData)
            }
        }
    }


}