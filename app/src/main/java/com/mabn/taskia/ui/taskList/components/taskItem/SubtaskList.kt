package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.tooling.preview.Preview
import com.mabn.taskia.domain.model.Task

@Composable
fun SubtaskList(
    subtasks: List<Task>,
    removeItemFunc: (Task) -> Unit,
    toggleStatusFun: (Task) -> Boolean
) {
    Column {
        subtasks.forEach { subtask ->
            key(subtask.id) {
                TaskItem(
                    task = subtask,
                    subtasks = null,
                    removeItemFunc = { removeItemFunc(subtask) },
                    toggleStatusFun = { toggleStatusFun(subtask) },
                    onClick = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun SubtaskList_Preview() {
    MaterialTheme {
        Surface {
            SubtaskList(
                subtasks = listOf(Task(title = "Bread"), Task(title = "Eggs")),
                removeItemFunc = {},
                toggleStatusFun = { true }
            )
        }
    }
}