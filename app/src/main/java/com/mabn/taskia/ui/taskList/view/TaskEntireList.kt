package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.util.LocalDateTimeConverter
import com.mabn.taskia.ui.taskList.ListType
import com.mabn.taskia.ui.taskList.TaskListViewModel

@Composable
fun TaskEntireList(
    viewModel: TaskListViewModel,
    listType: ListType,
    showTaskForm: (Task) -> Unit,
) {
    viewModel.setListType(listType)
    val context = LocalContext.current
    val overdueTasks = viewModel.overdueTasks.observeAsState()
    val tasks = viewModel.tasks.observeAsState()

    Column(Modifier.padding(vertical = 10.dp)) {
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

            val grouped = tasks.value!!.groupBy { it.first.endDate }
            grouped.keys.forEach {
                if (it != null) {
                    TaskListSection(
                        text = LocalDateTimeConverter.dateToString(it, context),
                        items = grouped[it]!!,
                        onTaskRemove = { task ->
                            viewModel.removeTask(task)
                        },
                        toggleStatusFun = { task ->
                            viewModel.toggleTaskStatus(task)
                        },
                        onItemClick = showTaskForm
                    )
                } else {
                    TaskListSection(
                        text = stringResource(id = R.string.unscheduled_tasks),
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
            }
        }
        if (
            (listType == ListType.Today && overdueTasks.value?.isEmpty() == true && tasks.value?.isEmpty() == true) ||
            (listType != ListType.Today && tasks.value?.isEmpty() == true)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                NoTasks(listType)
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