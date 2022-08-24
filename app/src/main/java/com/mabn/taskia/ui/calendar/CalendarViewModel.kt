package com.mabn.taskia.ui.calendar

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.persistence.repository.TagRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.util.ViewModelsCommunicationBridge
import com.mabn.taskia.ui.common.base.WithFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val defaultDateBridge: ViewModelsCommunicationBridge<LocalDate>,
    tagRepository: TagRepository
) : WithFilter(tagRepository) {

    private var _allTasks: List<Pair<Task, Pair<List<Task>, List<Tag>>>> = listOf()

    init {
        fetchTasks(LocalDate.now())
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.DateChanged -> {
                defaultDateBridge.onDispatchMessage(event.date)
                fetchTasks(event.date)
            }
            is CalendarEvent.FilterTagsChanged -> setFilterTags(event.tags)
        }
    }

    fun refreshTasks() {
        fetchTasks(LocalDate.now())
    }

    private fun fetchTasks(date: LocalDate) {
        viewModelScope.launch {
            taskRepository.getByDateAndUnscheduled(date).collect { result ->
                _allTasks = result.map { Pair(it.task, Pair(it.subtasks, it.tags)) }
                filter(_allTasks, filteredTasks)

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