package com.mabn.taskia.ui.taskList

import android.content.Intent
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Message
import com.mabn.taskia.domain.model.MessageType
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.network.TasksSynchronizer
import com.mabn.taskia.domain.persistence.repository.MessageRepository
import com.mabn.taskia.domain.persistence.repository.TagRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.util.ContextProvider
import com.mabn.taskia.domain.util.IntentAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.streams.toList

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tagRepository: TagRepository,
    private val messageRepository: MessageRepository,
    private val contextProvider: ContextProvider,
    private val tasksSynchronizer: TasksSynchronizer
) : ViewModel() {
    private val _tasks = MutableLiveData<SnapshotStateMap<Task, Pair<List<Task>, List<Tag>>>>()
    val tasks: LiveData<SnapshotStateList<Pair<Task, List<Task>>>>
        get() {
            return when (_listType.value) {
                ListType.Today -> _filteredTodayTasks
                ListType.Completed -> _filteredCompletedTasks
                ListType.Unscheduled -> _filteredUnscheduledTasks
                ListType.Upcoming -> _filteredUpcomingTasks
                else -> MutableLiveData()
            }
        }

    private val _overdueTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>>()
    val overdueTasks: LiveData<SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>> =
        _overdueTasks

    private val _todayTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>>()
    private val _filteredTodayTasks = MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()
    private val _unscheduledTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>>()
    private val _filteredUnscheduledTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()
    private val _completedTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>>()
    private val _filteredCompletedTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()
    private val _upcomingTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>>()
    private val _filteredUpcomingTasks =
        MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>()


    private val _allTags = MutableLiveData<List<Tag>>()
    val allTags: LiveData<List<Tag>> = _allTags

    private val _filterTags = MutableLiveData<List<Tag>>()
    val filterTags: LiveData<List<Tag>> = _filterTags

    private val _listType = MutableStateFlow<ListType>(ListType.Today)
    private val _jobs = mutableListOf<Job>()


    private var recentlyRemovedTask: Task? = null
    private var recentlyChangedStatusTask: Task? = null

    init {
        collectTasks()
        collectTags()
    }

    private fun collectTags() {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.getAll().collect {
                _allTags.postValue(it)
                filter(_todayTasks.value, _filteredTodayTasks)
                filter(_completedTasks.value, _filteredCompletedTasks)
                filter(_upcomingTasks.value, _filteredUpcomingTasks)
                filter(_unscheduledTasks.value, _filteredUnscheduledTasks)
            }
        }
    }

    private fun filter(
        list: SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>?,
        targetLiveData: MutableLiveData<SnapshotStateList<Pair<Task, List<Task>>>>
    ) {
        val snapshotList = SnapshotStateList<Pair<Task, List<Task>>>()
        snapshotList.addAll(filterByTags(list))
        targetLiveData.postValue(snapshotList)
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
                val newTasksData = SnapshotStateMap<Task, Pair<List<Task>, List<Tag>>>()
                result.forEach {
                    newTasksData[it.task] = Pair(it.subtasks, it.tags)
                }
                val prevValue = _tasks.value
                _tasks.postValue(newTasksData)
                if (prevValue == null) {
                    contextProvider.getContext()
                        .sendBroadcast(Intent(IntentAction.ACTION_APP_LOADED))
                }
            }
        })
    }

    fun setFilterTags(tags: List<Tag>) {
        _filterTags.postValue(tags)
    }

    private fun filterByTags(list: SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>?): List<Pair<Task, List<Task>>> {
        if (_filterTags.value.isNullOrEmpty()) {
            return list?.map {
                Pair(it.first, it.second.first)
            } ?: listOf()
        }
        return list?.stream()?.filter {
            it.second.second.intersect(_filterTags.value!!).size == _filterTags.value!!.size
        }
            ?.map {
                Pair(it.first, it.second.first)
            }
            ?.toList() ?: listOf()
    }

    private fun collectTodayTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getTodayTasks().collect { result ->
                val todayTaskList = SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>()
                result.forEach {
                    todayTaskList.add(Pair(it.task, Pair(it.subtasks, it.tags)))
                }
                val prevValue = _todayTasks.value
                _todayTasks.postValue(todayTaskList)
                filter(_todayTasks.value, _filteredTodayTasks)
                if (prevValue == null) {
                    contextProvider.getContext()
                        .sendBroadcast(Intent(IntentAction.ACTION_APP_LOADED))
                }
            }
        })
    }

    private fun collectUnscheduledTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAllUnscheduled().collect { result ->
                val unscheduledTaskList =
                    SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>()
                result.forEach {
                    unscheduledTaskList.add(Pair(it.task, Pair(it.subtasks, it.tags)))
                }
                val prevValue = _unscheduledTasks.value
                _unscheduledTasks.postValue(unscheduledTaskList)
                filter(_unscheduledTasks.value, _filteredUnscheduledTasks)
                if (prevValue == null) {
                    contextProvider.getContext()
                        .sendBroadcast(Intent(IntentAction.ACTION_APP_LOADED))
                }

            }
        })
    }

    private fun collectCompletedTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAllCompleted().collect { result ->
                val completedTaskList =
                    SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>()
                result.forEach {
                    completedTaskList.add(Pair(it.task, Pair(it.subtasks, it.tags)))
                }
                val prevValue = _completedTasks.value
                _completedTasks.postValue(completedTaskList)
                filter(_completedTasks.value, _filteredCompletedTasks)
                if (prevValue == null) {
                    contextProvider.getContext()
                        .sendBroadcast(Intent(IntentAction.ACTION_APP_LOADED))
                }
            }
        })
    }

    private fun collectUpcomingTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAllUpcoming().collect { result ->
                val upcomingTaskList =
                    SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>()
                result.forEach {
                    upcomingTaskList.add(Pair(it.task, Pair(it.subtasks, it.tags)))
                }
                val prevValue = _upcomingTasks.value
                _upcomingTasks.postValue(upcomingTaskList)
                filter(_upcomingTasks.value, _filteredUpcomingTasks)
                if (prevValue == null) {
                    contextProvider.getContext()
                        .sendBroadcast(Intent(IntentAction.ACTION_APP_LOADED))
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
            tasksSynchronizer.delete(task)
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

    fun toggleTaskStatus(task: Task): Boolean {
        val subtasksList = _tasks.value?.get(task)
        val allSubtasksCompleted =
            if (subtasksList?.first.isNullOrEmpty()) true else subtasksList?.first?.stream()
                ?.allMatch { it.status }
        viewModelScope.launch(Dispatchers.IO) {
            var newStatus = task.status
            if (subtasksList?.first.isNullOrEmpty()) {
                newStatus = !task.status
            } else {
                if (allSubtasksCompleted == true || task.status) {
                    newStatus = !task.status
                } else {
                    messageRepository.insertMessage(
                        Message(text = contextProvider.getString(R.string.uncompleted_tasks))
                    )
                }
            }

            if (newStatus != task.status) {
                task.status = newStatus
                if (newStatus) {
                    task.completionTime = LocalDateTime.now()
                }
                delay(500)
                taskRepository.update(task)
                tasksSynchronizer.updateTask(task)
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
        if (!(allSubtasksCompleted == true || task.status)) {
            return false
        }
        return true
    }

    private fun undoToggleStatus() {
        if (recentlyChangedStatusTask != null) {
            viewModelScope.launch(Dispatchers.IO) {
                recentlyChangedStatusTask!!.status = !recentlyChangedStatusTask!!.status
                taskRepository.update(recentlyChangedStatusTask!!)
                tasksSynchronizer.updateTask(recentlyChangedStatusTask!!)
            }
        }
    }

    private fun restoreRemovedTask() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyRemovedTask != null) {
                taskRepository.insertAll(recentlyRemovedTask!!)
                tasksSynchronizer.insert(recentlyRemovedTask!!)
            }
        }
    }


}