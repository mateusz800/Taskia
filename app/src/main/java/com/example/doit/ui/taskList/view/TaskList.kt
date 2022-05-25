package com.example.doit.ui.taskList.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doit.domain.model.Task
import com.example.doit.ui.taskList.TaskListViewModel

@Composable
fun TaskList(
    viewModel: TaskListViewModel,
    showTaskForm: (Task) -> Unit
) {
    val taskMap = viewModel.tasks.observeAsState()

    Column(Modifier.padding(30.dp)) {
        if (!taskMap.value.isNullOrEmpty()) {
            TaskList(
                taskMap = taskMap.value!!,
                onTaskRemove = { task ->
                    viewModel.removeTask(task)
                },
                toggleStatusFun = { task ->
                    viewModel.toggleTaskStatus(task)
                },
                onItemClick = showTaskForm
            )
        } else if (taskMap.value?.isEmpty() == true) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                NoTasks()
            }
        } else {
            // TODO : loading animation
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskList(
    taskMap: SnapshotStateMap<Task, List<Task>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Unit,
    onItemClick: (task: Task) -> Unit
) {
    val listState: LazyListState = rememberLazyListState()

    if (taskMap.keys.isNotEmpty()) {
        LazyColumn(
            state = listState,
        ) {
            items(taskMap.keys.toList()) { task ->
                key(task.id) {
                    Column(modifier = Modifier.animateItemPlacement()) {
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
        }
    }
}

@Preview
@Composable
private fun TaskList_Preview() {
    val taskMap = SnapshotStateMap<Task, List<Task>>()
    taskMap[Task(id = 1, title = "First task")] = listOf(Task(title = "subtask", parentId = 1))
    taskMap[Task(id = 2, title = "Second task")] = listOf()
    MaterialTheme {
        TaskList(
            taskMap = taskMap,
            onTaskRemove = {},
            toggleStatusFun = {},
            onItemClick = {}
        )
    }
}