package com.example.doit.ui.browseTasks.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doit.domain.model.Task

@Composable
public fun TaskItem(task: Task, subtasks: List<Task>?) {
    Column {
        TaskGeneralInfo(task.status, task.title)
        if (!subtasks.isNullOrEmpty()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start=20.dp)
                .padding(vertical = 10.dp)
            ) {
                SubtasksList(subtasks = subtasks)
            }
        }
    }
}

@Composable
private fun TaskGeneralInfo(status: Boolean, title: String) {
    Row {
        CustomCheckbox(status)
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = title)
    }
}

@Composable
private fun SubtasksList(subtasks: List<Task>) {
    Column {
        subtasks.forEach { subtask ->
            TaskGeneralInfo(status = subtask.status, title = subtask.title)
        }
    }
}

@Composable
private fun CustomCheckbox(status: Boolean) {
    val checkedState = remember { mutableStateOf(status) }
    Checkbox(
        checked = checkedState.value,
        onCheckedChange = { checkedState.value = it }
    )
}

@Preview
@Composable
private fun TaskItem_Preview() {
    val task = Task(title = "Shopping")
    val subtasks = listOf(Task(title = "Bread"))
    MaterialTheme {
        TaskItem(task = task, subtasks = subtasks)
    }
}