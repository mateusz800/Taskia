package com.example.doit.ui.taskForm

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.R
import com.example.doit.domain.ContextProvider
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject
import kotlin.streams.toList

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val contextProvider: ContextProvider
) : ViewModel() {
    var isVisible = MutableStateFlow(false)
    private var _task: Task? = null

    private val _title = MutableStateFlow("")
    val title: StateFlow<String>
        get() = _title

    private val _dueTo = MutableStateFlow<LocalDateTime?>(null)
    private val _dueDay = MutableStateFlow(contextProvider.getString(R.string.no_deadline))
    val dueDay: StateFlow<String>
        get() = _dueDay

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _dueTo.collect {
                if (it != null) {
                    val day = it.dayOfMonth.toString() + "." +
                            it.month.toString()
                    _dueDay.emit(day)
                } else {
                    _dueDay.emit(contextProvider.getString(R.string.no_deadline))
                }
            }
        }
    }

    val subtasks = SnapshotStateList<Task>()

    fun onTitleChanged(title: String) {
        _title.value = title
    }

    fun updateDueToDate(value: String) {
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

    fun setTask(task: Task, context: Context) {
        _task = task
        _title.value = task.title
        _dueTo.value = task.endDate
        _dueDay.value = task.getEndDay(context) ?: ""
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
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    fun clear() {
        _task = null
        _title.value = ""
        _dueTo.value = null
        _dueDay.value = contextProvider.getString(R.string.no_deadline)
        subtasks.clear()
    }
}