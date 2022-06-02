package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mabn.taskia.domain.model.Task

@Composable
fun TaskListSection(
    text: String,
    items: SnapshotStateList<Pair<Task, List<Task>>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Unit,
    onItemClick: (task: Task) -> Unit
) {
    Column {
        if (text.isNotBlank()) {
            Text(
                text,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(15.dp)
            )
        }
        TaskList(
            taskList = items,
            onTaskRemove = onTaskRemove,
            toggleStatusFun = toggleStatusFun,
            onItemClick = onItemClick
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskList(
    taskList: List<Pair<Task, List<Task>>>,
    onTaskRemove: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Unit,
    onItemClick: (task: Task) -> Unit
) {
    val listState: LazyListState = rememberLazyListState()

    if (taskList.isNotEmpty()) {
        LazyColumn(
            state = listState,
        ) {
            items(taskList.toList()) {
                val task = it.first
                val subtasks = it.second
                key(task.id) {
                    Column(modifier = Modifier.animateItemPlacement()) {
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
