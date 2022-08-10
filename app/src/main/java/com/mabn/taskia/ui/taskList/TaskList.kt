package com.mabn.taskia.ui.taskList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.taskList.components.NoTasks
import com.mabn.taskia.ui.taskList.components.TaskListSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun TaskList(
    viewModel: TaskListViewModel,
    listType: ListType,
    showTaskForm: (Task) -> Unit,
) {
    val onEvent = viewModel::onEvent
    viewModel.setListType(listType)
    val tasks = viewModel.tasks.observeAsState()


    val grouped = tasks.value?.groupBy {
        if (it.first.status) {
            LocalDate.now().plusDays(1).atStartOfDay()
        }
        else if (it.first.endDate != null && it.first.endDate!!.isBefore(
                LocalDate.now().atStartOfDay()
            )
        ) {
            LocalDate.now().minusDays(1).atStartOfDay()
        } else if (it.first.endDate != null && it.first.endDate!!.isBefore(
                LocalDate.now().plusDays(1).atStartOfDay()
            )
        ) {
            LocalDate.now().atStartOfDay()
        } else LocalDate.now().plusDays(2).atStartOfDay()
    }

    Column(
        Modifier
            .padding(vertical = 10.dp)
    ) {
        AnimatedVisibility(
            visible = !tasks.value.isNullOrEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = 500))
        ) {
            TaskList(
                tasks = grouped ?: mapOf(),
                listType = listType,
                onEvent = onEvent,
                showTaskForm = showTaskForm
            )

        }
        if (tasks.value.isNullOrEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (tasks.value?.isEmpty() == true) {
                    NoTasks(listType)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskList(
    tasks: Map<LocalDateTime?, List<Pair<Task, List<Task>>>>,
    listType: ListType,
    onEvent: (ListEvent) -> Boolean,
    showTaskForm: (Task) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    LazyColumn(
        state = listState,
        modifier = Modifier
    ) {
        tasks.keys.sortedBy { it }.forEachIndexed { index, it ->
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
                        if (it != null && it.isBefore(LocalDate.now().atStartOfDay())) {
                            stringResource(id = R.string.overdue_tasks)
                        } else if (it != null && it.isBefore(
                                LocalDate.now().plusDays(1).atStartOfDay()
                            )
                        ) {
                            stringResource(id = R.string.today)

                        } else if(it != null && it.isBefore(LocalDate.now().plusDays(2).atStartOfDay())){
                            stringResource(id = R.string.completed)
                        }
                        else {
                            stringResource(id = R.string.future)
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
                        items = tasks[it]!!,
                        onTaskRemove = { task ->
                            onEvent(ListEvent.TaskRemoved(task))
                        },
                        toggleStatusFun = { task ->
                            onEvent(ListEvent.TaskStatusChanged(task))
                        },
                        onItemClick = { task ->
                            showTaskForm(task)
                            focusManager.clearFocus(force = true)
                        }
                    )
                } else {
                    TaskListSection(
                        items = tasks.values.flatten(),
                        onTaskRemove = { task ->
                            onEvent(ListEvent.TaskRemoved(task))
                        },
                        toggleStatusFun = { task ->
                            onEvent(ListEvent.TaskStatusChanged(task))
                        },
                        onItemClick = { task ->
                            showTaskForm(task)
                            focusManager.clearFocus(force = true)
                        }
                    )
                }
            }
        }

    }
}


@Preview
@Composable
private fun TaskList_Preview() {
    val taskMap: MutableMap<LocalDateTime?, List<Pair<Task, List<Task>>>> = mutableMapOf()
    taskMap[LocalDate.now().minusDays(1).atStartOfDay()] =
        listOf(
            Pair(
                Task(title = "Finish presentation file"),
                listOf()
            )
        )
    taskMap[LocalDate.now().atStartOfDay()] =
        listOf(Pair(Task(title = "Shopping"), listOf(Task(title = "bread", parentId = 1))))
    MaterialTheme {
        TaskList(
            tasks = taskMap,
            listType = ListType.Tasks,
            onEvent = { true },
            showTaskForm = {}
        )
    }
}