package com.mabn.taskia.ui.taskList

import android.content.Intent
import android.os.Build
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.MutableLiveData
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
import com.mabn.taskia.ui.common.base.WithFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    tagRepository: TagRepository,
    private val messageRepository: MessageRepository,
    private val contextProvider: ContextProvider,
    private val tasksSynchronizer: TasksSynchronizer
) : WithFilter(tagRepository) {
    private val _tasks = MutableLiveData<Map<Task, Pair<List<Task>, List<Tag>>>>()
    private var _allTasks: List<Pair<Task, Pair<List<Task>, List<Tag>>>> = listOf()
    private val _jobs = mutableListOf<Job>()


    private var recentlyRemovedTask: Task? = null
    private var recentlyChangedStatusTask: Task? = null

    init {
        collectTasks()
        collectTags()
    }

    fun onEvent(event: ListEvent): Boolean {
        when (event) {
            is ListEvent.TaskStatusChanged -> return toggleTaskStatus(event.task)
            is ListEvent.TaskRemoved -> removeTask(event.task)
            is ListEvent.FilterTagsChanged -> setFilterTags(event.tags)
        }
        return true
    }


    private fun collectTasks() {
        _jobs.add(viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAllUncompleted().collect { result ->
                val newTasksData = SnapshotStateMap<Task, Pair<List<Task>, List<Tag>>>()
                result.forEach {
                    newTasksData[it.task] = Pair(it.subtasks, it.tags)
                }
                _allTasks = result.map { Pair(it.task, Pair(it.subtasks, it.tags)) }
                val prevValue = _tasks.value
                _tasks.postValue(newTasksData)
                if (prevValue == null) {
                    contextProvider.getContext()
                        .sendBroadcast(Intent(IntentAction.ACTION_APP_LOADED))
                }
                val todayTaskList = SnapshotStateList<Pair<Task, Pair<List<Task>, List<Tag>>>>()
                result.forEach {
                    todayTaskList.add(Pair(it.task, Pair(it.subtasks, it.tags)))
                }
                filter(todayTaskList, filteredTasks)
            }
        })
    }


    private fun removeTask(task: Task) {
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

    private fun toggleTaskStatus(task: Task): Boolean {
        val subtasksList = _tasks.value?.get(task)
        val allSubtasksCompleted =
            if (subtasksList?.first.isNullOrEmpty()) true else subtasksList?.first?.all { it.status }
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        task.completionTime = LocalDateTime.now()
                    }
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
                } else if (newStatus) {
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

    override fun setFilterTags(tags: List<Tag>) {
        super.setFilterTags(tags)
        if (tags.isEmpty()) {
            val taskList = SnapshotStateList<Pair<Task, List<Task>>>()
            taskList.addAll(_allTasks.map { Pair(it.first, it.second.first) })
            filteredTasks.postValue(taskList)
        } else {
            filter(_allTasks, filteredTasks)
        }
    }
}