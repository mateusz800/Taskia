package com.mabn.taskia.ui.taskForm

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.model.TaskTag
import com.mabn.taskia.domain.network.TasksSynchronizer
import com.mabn.taskia.domain.persistence.repository.TagRepository
import com.mabn.taskia.domain.persistence.repository.TaskRepository
import com.mabn.taskia.domain.persistence.repository.TaskTagRepository
import com.mabn.taskia.domain.util.ContextProvider
import com.mabn.taskia.domain.util.dbConverter.LocalDateTimeConverter
import com.mabn.taskia.ui.taskList.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject
import kotlin.streams.toList

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tagRepository: TagRepository,
    private val taskTagRepository: TaskTagRepository,
    private val contextProvider: ContextProvider,
    private val tasksSynchronizer: TasksSynchronizer
) : ViewModel() {
    var isVisible = MutableStateFlow(false)
    private var _task: Task? = null

    private val _dataChanged = MutableStateFlow(false)
    val dataChanged: StateFlow<Boolean> = _dataChanged

    private var _currentList: ListType = ListType.Today
    private val _title = MutableStateFlow("")
    val title: StateFlow<String>
        get() = _title

    private val _dueTo = MutableStateFlow<LocalDateTime?>(null)
    private val _dueDay = MutableStateFlow(contextProvider.getString(R.string.no_deadline))
    val dueDay: StateFlow<String>
        get() = _dueDay

    val subtasks = SnapshotStateList<Task>()
    val tags = SnapshotStateList<Tag>()

    init {
        tags.add(Tag(value = ""))
        viewModelScope.launch(Dispatchers.IO) {
            _dueTo.collect {
                if (it != null) {
                    _dueDay.emit(
                        LocalDateTimeConverter.dateToString(
                            it,
                            contextProvider.getContext()
                        )
                    )
                } else {
                    _dueDay.emit(contextProvider.getString(R.string.no_deadline))
                }
            }
        }
    }


    fun onTitleChanged(title: String) {
        _title.value = title
        _dataChanged.value = true
    }

    fun updateDueToDate(value: String) {
        _dataChanged.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (value.isBlank()) {
                _dueTo.emit(null)
                return@launch
            }
            val processedValue: LocalDateTime = try {
                LocalDateTime.parse(value)
            } catch (e: DateTimeParseException) {
                LocalDate.parse(value).atStartOfDay()
            }
            _dueTo.emit(processedValue)
        }
    }


    fun verifyData(): Boolean {
        if (_title.value.isEmpty()) {
            return false
        }
        return true
    }

    fun saveTask() {
        val task: Task = if (_task == null) {
            Task(title = title.value, endDate = _dueTo.value)
        } else {
            _task!!.copy(
                title = title.value,
                endDate = _dueTo.value
            )
        }
        val subtaskList = subtasks.parallelStream()
            .filter { it.title.isNotEmpty() }
            .toList()
        val insertedTags = mutableListOf<Tag>()
        val tagsList = tags.parallelStream()
            .filter { it.value.isNotBlank() }
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
            tasksSynchronizer.updateTask(task)

            tagsList.forEach { tag ->
                insertedTags.add(tag.copy(id = tagRepository.insert(tag)))
            }

            val prevTags = taskTagRepository.getTags(task)
            val tagsToRemove = prevTags.filterNot { insertedTags.contains(it) }

            tagsToRemove.forEach {
                taskTagRepository.deleteTagFromTask(tag = it, task = task)
            }
            taskTagRepository.insert(
                *insertedTags.stream().map { TaskTag(parentId, it.id) }.toList().toTypedArray()
            )


        }
    }

    fun setCurrentList(currentList: ListType) {
        _currentList = currentList
        initDueToDefaultValue()
    }

    private fun initDueToDefaultValue() {
        viewModelScope.launch(Dispatchers.IO) {
            when (_currentList) {
                is ListType.Today -> {
                    val date = LocalDate.now().atStartOfDay()
                    _dueTo.emit(date)
                    _dueDay.emit(
                        LocalDateTimeConverter.dateToString(
                            date,
                            contextProvider.getContext()
                        )
                    )

                }
                else -> {
                    _dueTo.emit(null)
                    _dueDay.emit(contextProvider.getString(R.string.no_deadline))
                }
            }
        }
    }

    fun setTask(task: Task, context: Context) {
        _task = task
        _dataChanged.value = false
        _title.value = task.title
        _dueTo.value = task.endDate
        _dueDay.value = task.getEndDay(context)
        viewModelScope.launch(Dispatchers.IO) {
            subtasks.clear()
            val temp = taskRepository.getSubtasks(task)
            subtasks.addAll(temp)
            tags.clear()
            val downloadedTags = taskTagRepository.getTags(task)
            tags.addAll(downloadedTags)
            tags.add(Tag(value = " "))
        }

    }

    fun addNewSubtask() {
        if (subtasks.isEmpty() || subtasks.last().title.isNotBlank()) {
            subtasks.add(Task(title = ""))
        }
    }

    fun addNewTag(focusOnNew: Boolean) {
        if (tags.isEmpty() || tags.last().value.isNotBlank()) {
            if (focusOnNew) {
                tags.add(Tag(value = ""))
            } else {
                tags.add(Tag(value = " "))
            }

        }
    }

    fun updateSubtaskTitle(subtask: Task, newTitle: String) {
        try {
            subtasks[subtasks.indexOf(subtask)] =
                subtasks[subtasks.indexOf(subtask)].copy(title = newTitle)
            _dataChanged.value = true
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    fun updateTagValue(tag: Tag, newValue: String) {
        try {
            if (newValue.isBlank()) {
                tags.remove(tag)
            } else {
                tags[tags.indexOf(tag)] = tag.copy(value = newValue)
            }
            _dataChanged.value = true
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    fun clear() {
        _task = null
        _title.value = ""
        initDueToDefaultValue()
        subtasks.clear()
        tags.clear()
        tags.add(Tag(value = ""))
        _dataChanged.value = false
    }
}