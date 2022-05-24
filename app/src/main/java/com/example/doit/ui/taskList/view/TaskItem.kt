package com.example.doit.ui.taskList.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doit.domain.model.Task

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    task: Task,
    subtasks: List<Task>?,
    removeItemFunc: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Unit,
    onClick: (task: Task) -> Unit
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToEnd) {
            removeItemFunc(task)
        }
        false
    })
    Column {
        SwipeToDismiss(state = dismissState, background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RectangleShape)
                    .background(Color.Red)
            )

        }) {

            TaskGeneralInfo(
                task.status,
                task.title,
                onCheck = {
                    toggleStatusFun(task)
                },
                onClick = {
                    onClick(task)
                }
            )
        }
        if (!subtasks.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
                    .padding(vertical = 10.dp)
            ) {
                SubtasksList(
                    subtasks = subtasks,
                    removeItemFunc = removeItemFunc,
                    toggleStatusFun = toggleStatusFun
                )
            }
        }
    }
}

@Composable
fun TaskGeneralInfo(
    status: Boolean,
    title: String,
    onCheck: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckbox(status) {
            onCheck()
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = title,
            modifier = Modifier.clickable { onClick() },
            textDecoration = if (status) TextDecoration.LineThrough else TextDecoration.None
        )
    }
}

@Composable
private fun SubtasksList(
    subtasks: List<Task>,
    removeItemFunc: (Task) -> Unit,
    toggleStatusFun: (Task) -> Unit
) {
    Column {
        subtasks.forEach { subtask ->
            key(subtask.id) {
                TaskItem(
                    task = subtask,
                    subtasks = null,
                    removeItemFunc = { removeItemFunc(subtask) },
                    toggleStatusFun = { toggleStatusFun(subtask) },
                    onClick = {/* do nothing */ }
                )
            }
        }
    }
}

@Composable
fun CustomCheckbox(status: Boolean, onCheck: () -> Unit) {
    Checkbox(
        checked = status,
        onCheckedChange = {
            onCheck()
        }
    )
}

@Preview
@Composable
private fun TaskItem_Preview() {
    val task = Task(title = "Shopping")
    val subtasks = listOf(Task(title = "Bread"))
    MaterialTheme {
        TaskItem(
            task = task,
            subtasks = subtasks,
            removeItemFunc = {},
            toggleStatusFun = {},
            onClick = {}
        )
    }
}