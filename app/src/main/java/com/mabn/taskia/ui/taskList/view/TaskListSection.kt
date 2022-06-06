package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.mabn.taskia.domain.model.Task

@Composable
fun TaskListSection(
    items: List<Pair<Task, List<Task>>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Boolean,
    onItemClick: (task: Task) -> Unit
) {
    TaskList(
        taskList = items,
        onTaskRemove = onTaskRemove,
        toggleStatusFun = toggleStatusFun,
        onItemClick = onItemClick
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskList(
    taskList: List<Pair<Task, List<Task>>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Boolean,
    onItemClick: (task: Task) -> Unit
) {
    val listState: LazyListState = rememberLazyListState()

    if (taskList.isNotEmpty()) {
        Column(

        ) {
            taskList.toList().forEach {
                val task = it.first
                val subtasks = it.second
                key(task.id) {
                    Column(modifier = Modifier/* Modifier.animateItemPlacement()*/) {
                        TaskItem(
                            task = task,
                            subtasks = subtasks,
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
