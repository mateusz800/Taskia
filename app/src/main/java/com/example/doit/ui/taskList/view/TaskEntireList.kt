package com.example.doit.ui.taskList.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.doit.R
import com.example.doit.domain.model.Task
import com.example.doit.ui.taskList.ListType
import com.example.doit.ui.taskList.TaskListViewModel

@Composable
fun TaskEntireList(
    viewModel: TaskListViewModel,
    listType: ListType,
    showTaskForm: (Task) -> Unit,
) {
    viewModel.setListType(listType)

    val overdueTasks = viewModel.overdueTasks.observeAsState()
    val tasks = viewModel.tasks.observeAsState()




    Column(Modifier.padding(vertical = 30.dp)) {
        if (
            !overdueTasks.value.isNullOrEmpty() &&
            listType == ListType.Today
        ) {
            TaskListSection(
                text = stringResource(id = R.string.overdue_tasks),
                items = overdueTasks.value!!,
                onTaskRemove = { task ->
                    viewModel.removeTask(task)
                },
                toggleStatusFun = { task ->
                    viewModel.toggleTaskStatus(task)
                },
                onItemClick = showTaskForm
            )
        }
        if (!tasks.value.isNullOrEmpty()) {
            TaskListSection(
                text = stringResource(id = listType.textId),
                items = tasks.value!!,
                onTaskRemove = { task ->
                    viewModel.removeTask(task)
                },
                toggleStatusFun = { task ->
                    viewModel.toggleTaskStatus(task)
                },
                onItemClick = showTaskForm
            )
        }
        if (
            overdueTasks.value.isNullOrEmpty() &&
            tasks.value.isNullOrEmpty()
        ) {
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


/*
@Preview
@Composable
private fun TaskList_Preview() {
    val taskMap = SnapshotStateMap<Task, List<Task>>()
    taskMap[Task(id = 1, title = "First task")] = listOf(Task(title = "subtask", parentId = 1))
    taskMap[Task(id = 2, title = "Second task")] = listOf()
    MaterialTheme {
        TaskList(
            taskList = taskMap,
            onTaskRemove = {},
            toggleStatusFun = {},
            onItemClick = {}
        )
    }
}
 */