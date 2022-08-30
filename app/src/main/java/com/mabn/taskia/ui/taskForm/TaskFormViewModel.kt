package com.mabn.taskia.ui.taskForm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.mabn.taskia.domain.util.ViewModelsCommunicationBridge
import com.mabn.taskia.domain.util.dbConverter.LocalDateTimeConverter
import com.mabn.taskia.domain.util.dbConverter.LocalTimeConverter
import com.mabn.taskia.domain.util.extension.toFormattedString
import com.mabn.taskia.ui.taskList.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException
import javax.inject.Inject
import kotlin.streams.toList

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tagRepository: TagRepository,
    private val taskTagRepository: TaskTagRepository,
    private val contextProvider: ContextProvider,
    private val tasksSynchronizer: TasksSynchronizer,
    defaultDateBridge: ViewModelsCommunicationBridge<LocalDate>
) : ViewModel() {
    private val _formState: MutableLiveData<FormState> = MutableLiveData(FormState())
    val formState: LiveData<FormState> = _formState


    var isVisible = MutableLiveData(false)
    private var _task: Task? = null
    private var _currentList: ListType = ListType.Tasks

    private var _defaultDate: LocalDate? = null

    var showUnsavedChangesDialog = MutableLiveData(false)
    var timeDialogOpened: Boolean = false

    init {
        clear()
        defaultDateBridge.registerCallback {
            _defaultDate = it
        }
    }

    fun onEvent(event: FormEvent) {
        when (event) {
            is FormEvent.TitleChanged -> updateTitle(event.newTitle)
            is FormEvent.TaskDateChanged -> updateDueToDate(event.date)
            is FormEvent.TaskTimeChanged -> updateStartTime(event.time)
            is FormEvent.AddNewSubtask -> addNewSubtask()
            is FormEvent.SubtaskTitleChanged -> updateSubtaskTitle(event.subtask, event.newTitle)
            is FormEvent.AddNewTag -> addNewTag(event.forceFocus)
            is FormEvent.TagValueChanged -> updateTagValue(event.tag, event.newValue)
            is FormEvent.Submit -> saveTask()
            is FormEvent.TimeDialogOpened -> timeDialogOpened = event.status
        }
    }

    fun verifyData(): Boolean {
        return _formState.value?.validateData() == true
    }

    fun setCurrentList(currentList: ListType) {
        _currentList = currentList
    }

    fun setTask(task: Task, context: Context) {
        _task = task
        viewModelScope.launch {
            val subtask = taskRepository.getSubtasks(task)
            val tags = taskTagRepository.getTags(task).toMutableList()
            tags.add(Tag(value = " "))
            _formState.postValue(
                FormState(
                    title = task.title,
                    dayLabel = task.getEndDay(context),
                    timeLabel = task.startTime.toFormattedString(context),
                    subtasks = subtask,
                    tags = tags,
                    dataChanged = false
                )
            )
        }
    }

    fun clear() {
        _task = null
        runBlocking {
            _formState.postValue(
                FormState(
                    dayLabel = if (_defaultDate == null) contextProvider.getString(R.string.no_deadline) else LocalDateTimeConverter.dateToString(
                        _defaultDate!!.atStartOfDay(),
                        contextProvider.getContext(),
                    ),
                    timeLabel = contextProvider.getString(R.string.no_time),
                    dataChanged = false
                )
            )
        }
    }

    fun saveTask() {
        var task = Task(
            title = _formState.value!!.title,
            endDate = LocalDateTimeConverter.stringToDate(
                _formState.value!!.dayLabel,
                context = contextProvider.getContext()
            ),
            startTime = LocalTimeConverter.stringToLocalTime(_formState.value?.timeLabel ?: "")
        )
        if (_task != null) {
            task = task.copy(
                id = _task!!.id,
                status = _task!!.status,
                completionTime = _task!!.completionTime
            )
        }

        val subtaskList = _formState.value?.subtasks?.filter { it.title.isNotEmpty() }
            ?.toList()
        viewModelScope.launch(Dispatchers.IO) {
            if (subtaskList.isNullOrEmpty()) {
                taskRepository.getSubtasks(task).forEach {
                    taskRepository.delete(it)
                }
            }
        }
        val insertedTags = mutableListOf<Tag>()
        val tagsList = _formState.value?.tags?.filter { it.value.isNotBlank() }
            ?.toList()
        viewModelScope.launch(Dispatchers.IO) {
            val parentId =
                if (task.id == 0L) {
                    taskRepository.insertAll(task)[0]
                } else {
                    task.id
                }
            subtaskList?.forEach {
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

            tagsList?.forEach { tag ->
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
            _formState.postValue(_formState.value?.copy(dataChanged = false))
        }
    }

    private fun updateTitle(title: String) {
        _formState.postValue(_formState.value?.copy(title = title, dataChanged = true))
    }

    private fun updateDueToDate(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (value.isBlank()) {
                _formState.postValue(
                    _formState.value?.copy(
                        dayLabel = contextProvider.getString(R.string.no_deadline),
                        dataChanged = true
                    )
                )
                return@launch
            }
            val processedValue: LocalDateTime = try {
                LocalDateTime.parse(value)
            } catch (e: DateTimeParseException) {
                LocalDate.parse(value).atStartOfDay()
            }
            _formState.postValue(
                _formState.value?.copy(
                    dayLabel = LocalDateTimeConverter.dateToString(
                        processedValue,
                        contextProvider.getContext(),
                    ),
                    dataChanged = true
                )
            )
        }
    }

    private fun updateStartTime(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val processedValue: LocalTime? = try {
                LocalTime.parse(value)
            } catch (e: DateTimeParseException) {
                null
            }
            _formState.postValue(
                _formState.value?.copy(
                    timeLabel = processedValue.toFormattedString(
                        contextProvider.getContext()
                    ),
                    dataChanged = true
                )
            )
        }
    }

    private fun addNewSubtask() {
        if (_formState.value?.subtasks.isNullOrEmpty()) {
            _formState.postValue(_formState.value?.copy(subtasks = listOf(Task(title = ""))))
        } else if (_formState.value?.subtasks?.last()?.title?.isNotBlank() == true) {
            _formState.postValue(
                _formState.value?.copy(
                    subtasks = listOf(
                        *_formState.value?.subtasks!!.toTypedArray(),
                        Task(title = "")
                    )
                )
            )
        }
    }

    private fun addNewTag(focusOnNew: Boolean) {
        var value = " "
        if (focusOnNew) {
            value = ""
        }
        if (_formState.value?.tags.isNullOrEmpty()) {
            _formState.postValue(_formState.value?.copy(tags = listOf(Tag(value = value))))
        } else if (_formState.value?.tags?.last()?.value?.isNotBlank() == true) {
            _formState.postValue(
                _formState.value?.copy(
                    tags = listOf(
                        *_formState.value?.tags!!.toTypedArray(),
                        Tag(value = value)
                    )
                )
            )
        }
    }

    private fun updateSubtaskTitle(subtask: Task, newTitle: String) {
        val subtaskList = _formState.value?.subtasks?.toMutableList()
        if (subtaskList != null) {
            try {
                subtaskList[subtaskList.indexOf(subtask)] =
                    subtaskList[subtaskList.indexOf(subtask)].copy(title = newTitle)
                _formState.postValue(
                    _formState.value?.copy(
                        subtasks = subtaskList,
                        dataChanged = true
                    )
                )
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.localizedMessage?.let { Log.i("TaskFormViewModel", it) }
            }
        }

    }

    private fun updateTagValue(tag: Tag, newValue: String) {
        val tagList = _formState.value?.tags?.toMutableList()
        if (tagList != null) {
            try {
                val index = tagList.indexOf(tag)
                tagList[index] = tagList[index].copy(value = newValue)
                _formState.postValue(_formState.value?.copy(tags = tagList, dataChanged = true))
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.localizedMessage?.let { Log.i("TaskFormViewModel", it) }
            }
        }
    }
}