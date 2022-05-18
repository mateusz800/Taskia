package com.example.doit.ui.browseTasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.domain.model.Message
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.repository.MessageRepository
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.LinkedHashMap

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<LinkedHashMap<Task, List<Task>>>()
    val tasks: LiveData<LinkedHashMap<Task, List<Task>>>
        get() = _tasks
    private var recentlyRemovedTask: Task? = null

    init {
        //initSampleData()
        collectTasks()
    }

    fun removeTask(task: Task) {
        recentlyRemovedTask = task
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.remove(task)
            messageRepository.insertMessage(Message(
                text = "Task removed",
                actionText = "undo",
                actionFun = { restoreRemovedTask() }
            ))
        }
    }

    private fun restoreRemovedTask() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyRemovedTask != null) {
                taskRepository.insertAll(recentlyRemovedTask!!)
            }
        }
    }

    private fun initSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            val parent = Task(id = 1, title = "Shopping")
            val children = Task(title = "bread", parentId = 1)
            taskRepository.insertAll(parent, children)
        }
    }

    private fun collectTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAll().collect { result ->
                val newTasksData = LinkedHashMap<Task, List<Task>>()
                result.forEach {
                    newTasksData[it.task] = it.subtasks
                }
                _tasks.postValue(newTasksData)
            }
        }
    }


}