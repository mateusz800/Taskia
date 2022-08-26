package com.mabn.taskia.ui.taskList.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.taskList.components.taskItem.TaskItem
import java.time.LocalDate

@Composable
fun TaskListSection(
    items: List<Pair<Task, List<Task>>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Boolean,
    showDates: Boolean = false,
    onItemClick: (task: Task) -> Unit,
) {
    TaskList(
        taskList = items,
        onTaskRemove = onTaskRemove,
        toggleStatusFun = toggleStatusFun,
        showDates = showDates,
        onItemClick = onItemClick
    )
}


@Composable
private fun TaskList(
    taskList: List<Pair<Task, List<Task>>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Boolean,
    showDates: Boolean = true,
    onItemClick: (task: Task) -> Unit
) {
    if (taskList.isNotEmpty()) {
        Column {
            taskList.toList().forEach {
                val task = it.first
                val subtasks = it.second
                key(task.id) {
                    Column(modifier = Modifier.testTag("task_list")/* Modifier.animateItemPlacement()*/) {
                        TaskItem(
                            task = task,
                            subtasks = subtasks,
                            removeItemFunc = onTaskRemove,
                            toggleStatusFun = toggleStatusFun,
                            showDate = showDates,
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
private fun TaskListSection_Preview() {
    MaterialTheme {
        Surface {
            TaskListSection(
                items = listOf(
                    Pair(
                        Task(
                            title = "Some task",
                            endDate = LocalDate.now().minusDays(1).atStartOfDay()
                        ), listOf()
                    ),
                    Pair(Task(title = "Second task"), listOf())
                ),
                onTaskRemove = {},
                toggleStatusFun = { true },
                onItemClick = {}
            )
        }
    }
}