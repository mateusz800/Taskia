package com.example.doit.ui.browseTasks.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.doit.domain.model.Task
import com.example.doit.ui.browseTasks.TaskListViewModel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

@Composable
fun TaskList(
    viewModel: TaskListViewModel,
    showTaskForm: (Task) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val taskHashMap = viewModel.tasks.observeAsState()
    if (taskHashMap.value != null) {
        TaskList(
            taskMap = taskHashMap.value!!,
            onTaskRemove = { task ->
                viewModel.removeTask(task)
            },
            toggleStatusFun = { task ->
                viewModel.toggleTaskStatus(task)
            },
            onItemClick = showTaskForm
        )

    }
}

@Composable
private fun TaskList(
    taskMap: Map<Task, List<Task>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Unit,
    onItemClick: (task: Task) -> Unit
) {
    LazyColumn {
        items(taskMap.keys.toList()) { task ->
            TaskItem(
                task = task,
                subtasks = taskMap[task],
                removeItemFunc = onTaskRemove,
                toggleStatusFun = toggleStatusFun,
                onClick = { onItemClick(task) }
            )
        }
    }
}

@Preview
@Composable
private fun TaskList_Preview() {
    val taskMap = HashMap<Task, List<Task>>()
    taskMap[Task(id = 1, title = "First task")] = listOf(Task(title = "subtask", parentId = 1))
    taskMap[Task(id = 2, title = "Second task")] = listOf()
    MaterialTheme {
        TaskList(
            taskMap = taskMap.toSortedMap(compareBy { it.order }),
            onTaskRemove = {},
            toggleStatusFun = {},
            onItemClick = {}
        )
    }
}