package com.mabn.taskia.ui.taskList

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Message
import com.mabn.taskia.domain.model.MessageType
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.repository.MessageRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.util.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val messageRepository: MessageRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _tasks = MutableLiveData<SnapshotStateMap<Task, List<Task>>>()
    val tasks: LiveData<SnapshotStateList<Pair<Task, List<Task>>>>
        get() {
            return when (_listType.value) {
                is ListType.Today -> _todayTasks
                ListType.Completed -> _completedTasks
                ListType.Unscheduled -> _unscheduledTasks
                ListType.Upcoming -> _upcomingTasks
            }
        }

    private val _overdueTasks = MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()
    val overdueTasks: LiveData<SnapshotStateList<Pair<Task, List<Task>>>> = _overdueTasks

    private val _todayTasks = MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()
    private val _unscheduledTasks = MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()
    private val _completedTasks = MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()
    private val _upcomingTasks = MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()

    private val _listType = MutableStateFlow<ListType>(ListType.Today)
    private val _jobs = mutableListOf<Job>()


    private var recentlyRemovedTask: Task? = null
    private var recentlyChangedStatusTask: Task? = null

    init {
        collectTasks()
    }

    private fun collectTasks() {
        when (_listType.value) {
            is ListType.Today -> collectTodayTasks()
            is ListType.Unscheduled -> collectUnscheduledTasks()
            is ListType.Completed -> collectCompletedTasks()
            is ListType.Upcoming -> collectUpcomingTasks()
        }
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAll().collect { result ->
                val newTasksData = SnapshotStateMap<Task, List<Task>>()
                result.forEach {
                    newTasksData[it.task] = it.subtasks
                }
                val prevValue = _tasks.value
                _tasks.postValue(newTasksData)
                if (prevValue == null) {
                    messageRepository.insertMessage(
                        Message(
                            text = "Tasks loaded",
                            type = MessageType.LOADED_EVENT
                        )
                    )
                }
            }
        })
    }


    private fun collectTodayTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getTodayTasks().collect { result ->
                val todayTaskList = SnapshotStateList<Pair<Task, List<Task>>>()
                result.forEach {
                    todayTaskList.add(Pair(it.task, it.subtasks))
                }
                val prevValue = _todayTasks.value
                _todayTasks.postValue(todayTaskList)
                if (prevValue == null) {
                    messageRepository.insertMessage(
                        Message(
                            text = "Tasks loaded",
                            type = MessageType.LOADED_EVENT
                        )
                    )
                }
            }
        })
    }

    private fun collectUnscheduledTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAllUnscheduled().collect { result ->
                val unscheduledTaskList = SnapshotStateList<Pair<Task, List<Task>>>()
                result.forEach {
                    unscheduledTaskList.add(Pair(it.task, it.subtasks))
                }
                val prevValue = _unscheduledTasks.value
                _unscheduledTasks.postValue(unscheduledTaskList)
                if (prevValue == null) {
                    messageRepository.insertMessage(
                        Message(
                            text = "Tasks loaded",
                            type = MessageType.LOADED_EVENT
                        )
                    )
                }

            }
        })
    }

    private fun collectCompletedTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAllCompleted().collect { result ->
                val completedTaskList = SnapshotStateList<Pair<Task, List<Task>>>()
                result.forEach {
                    completedTaskList.add(Pair(it.task, it.subtasks))
                }
                val prevValue = _completedTasks.value
                _completedTasks.postValue(completedTaskList)
                if (prevValue == null) {
                    messageRepository.insertMessage(
                        Message(
                            text = "Tasks loaded",
                            type = MessageType.LOADED_EVENT
                        )
                    )
                }
            }
        })
    }

    private fun collectUpcomingTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAllUpcoming().collect { result ->
                val upcomingTaskList = SnapshotStateList<Pair<Task, List<Task>>>()
                result.forEach {
                    upcomingTaskList.add(Pair(it.task, it.subtasks))
                }
                val prevValue = _upcomingTasks.value
                _upcomingTasks.postValue(upcomingTaskList)
                if (prevValue == null) {
                    messageRepository.insertMessage(
                        Message(
                            text = "Tasks loaded",
                            type = MessageType.LOADED_EVENT
                        )
                    )
                }
            }
        })
    }


    fun setListType(type: ListType) {
        _listType.value = type
        _jobs.forEach {
            it.cancel()
        }
        collectTasks()
    }

    fun removeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            recentlyRemovedTask = taskRepository.getById(task.id)
            if (taskRepository.delete(task)) {
                messageRepository.insertMessage(
                    Message(
                        text = contextProvider.getString(R.string.task_removed),
                        actionText = contextProvider.getString(R.string.undo),
                        actionFun = { restoreRemovedTask() },
                        type = MessageType.SNACKBAR
                    )
                )
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
                    newStatus = !task.status
                } else {
                    messageRepository.insertMessage(
                        Message(text = contextProvider.getString(R.string.uncompleted_tasks))
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
                    }
                } else {
                    recentlyChangedStatusTask = task
                    messageRepository.insertMessage(Message(
                        text = contextProvider.getString(R.string.task_completed),
                        actionText = contextProvider.getString(R.string.undo),
                        actionFun = {
                            undoToggleStatus()
                        }
                    ))
                }
            }
        }
    }

    private fun undoToggleStatus() {
        if (recentlyChangedStatusTask != null) {
            viewModelScope.launch(Dispatchers.IO) {
                recentlyChangedStatusTask!!.status = !recentlyChangedStatusTask!!.status
                taskRepository.update(recentlyChangedStatusTask!!)
            }
        }
    }

    private fun restoreRemovedTask() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyRemovedTask != null) {
                taskRepository.insertAll(recentlyRemovedTask!!)
            }
        }
    }


}