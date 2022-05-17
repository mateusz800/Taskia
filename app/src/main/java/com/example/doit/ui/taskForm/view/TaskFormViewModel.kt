package com.example.doit.ui.taskForm.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.domain.model.Task
import com.example.doit.domain.persistence.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _title = MutableStateFlow("")
    val title: StateFlow<String>
        get() = _title

    fun onTitleChanged(title: String) {
        _title.value = title
    }

    fun saveTask() {
        //TODO data verification
        val task = Task(title = title.value)
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insertAll(task)
        }
        clear()
    }
    private fun clear(){
        _title.value = ""
    }
}