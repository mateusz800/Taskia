package com.example.doit.ui.browseTasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doit.domain.model.Task
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
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<LinkedHashMap<Task, List<Task>>>()
    val tasks: LiveData<LinkedHashMap<Task, List<Task>>>
        get() = _tasks

    init {
        //initSampleData()
        collectTasks()
    }

    private fun initSampleData(){
        viewModelScope.launch(Dispatchers.IO){
            val parent = Task(id=1, title="Shopping")
            val children = Task(title="bread", parentId = 1)
            taskRepository.insertAll(parent, children)
        }
    }

    private fun collectTasks(){
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