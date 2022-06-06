package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.domain.util.LocalDateTimeConverter
import com.mabn.taskia.ui.taskList.ListType
import com.mabn.taskia.ui.taskList.TaskListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskEntireList(
    viewModel: TaskListViewModel,
    listType: ListType,
    showTaskForm: (Task) -> Unit,
) {
    viewModel.setListType(listType)
    val context = LocalContext.current
    val tasks = viewModel.tasks.observeAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    Column(
        Modifier
            .padding(vertical = 10.dp)
    ) {
        if (!tasks.value.isNullOrEmpty()) {
            val grouped = tasks.value!!.groupBy {
                if (it.first.endDate != null && it.first.endDate!!.isBefore(
                        LocalDate.now().atStartOfDay()
                    )
                ) LocalDate.now().minusDays(1).atStartOfDay()
                else it.first.endDate
            }
            LazyColumn(
                state = listState,
                modifier = Modifier
            ) {
                grouped.keys.forEachIndexed { index, it ->

                    stickyHeader(index) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.background)
                                .clickable {
                                    coroutineScope.launch(Dispatchers.Main) {
                                        listState.animateScrollToItem(
                                            index,
                                            configuration.screenHeightDp - 100
                                        )
                                    }
                                }
                        ) {
                            Text(
                                if (it != null && it.isBefore(
                                        LocalDate.now().atStartOfDay()
                                    )
                                ) {
                                    stringResource(id = R.string.overdue_tasks)
                                } else if (it != null) {
                                    LocalDateTimeConverter.dateToString(it, context)
                                } else {
                                    stringResource(id = R.string.unscheduled_tasks)
                                },
                                style = MaterialTheme.typography.h2,
                                modifier = Modifier
                                    .padding(horizontal = 15.dp)
                                    .padding(top = 15.dp)
                            )
                        }
                    }
                    item(it) {
                        if (it != null) {
                            TaskListSection(

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

            }
        }
        if (tasks.value?.isEmpty() == true) {
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