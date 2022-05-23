package com.example.doit.ui.taskForm.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.doit.domain.model.Task
import com.example.doit.ui.browseTasks.view.CustomCheckbox
import com.example.doit.ui.browseTasks.view.TaskGeneralInfo
import com.example.doit.ui.browseTasks.view.TaskItem

@Composable
fun Subtasks(subtasks: List<Task>?,
             addNewFun:()->Unit,
             onTitleChanged: (Task, String) -> Unit
) {
    Column {
        subtasks?.forEach { task ->
            SubtaskInput(
                task = task,
                onTitleChanged = onTitleChanged
            )
        }
        AddNewButton(addNewFun)
    }
}


@Composable
private fun SubtaskInput(
    task: Task,
    onTitleChanged: (Task, String) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomCheckbox(status = task.status) {
            // TODO
        }
        Spacer(modifier = Modifier.width(20.dp))
        TextField(
            value = task.title,
            onValueChange = { onTitleChanged(task, it) }
        )
    }
}

@Composable
private fun AddNewButton(onClick:()->Unit){
    Button(onClick = { onClick() }) {
        Text("Add subtask")
    }
}

@Preview
@Composable
private fun Subtasks_Preview() {
    val subtasks = listOf(
        Task(title = "bread"),
        Task(title = "milk"),
        Task(title = "")
    )
    MaterialTheme {
        Subtasks(
            subtasks = subtasks,
            addNewFun = {},
            onTitleChanged = {_, _ ->}
        )
    }
}

