package com.example.doit.ui.taskForm.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import com.example.doit.domain.model.Task
import com.example.doit.ui.browseTasks.view.TaskItem
import com.example.doit.ui.taskForm.TaskFormViewModel

@Composable
fun TaskForm(viewModel: TaskFormViewModel, closeFunc: () -> Unit) {
    val title by viewModel.title.collectAsState()
    val subtasks = viewModel.subtasks

    TaskForm(title = title,
        subtasks = subtasks,
        onTitleChanged = viewModel::onTitleChanged,
        updateSubtaskTitle = viewModel::updateSubtaskTitle,
        addNewSubtaskFun = viewModel::addNewSubtask,
        saveFun = {
            viewModel.saveTask()
            closeFunc()
        }
    )
}

@Composable
private fun TaskForm(
    title: String,
    subtasks: List<Task>,
    onTitleChanged: (String) -> Unit,
    updateSubtaskTitle: (Task, String) -> Unit,
    addNewSubtaskFun: () -> Unit,
    saveFun: () -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {
        TextField(value = title, onValueChange = { onTitleChanged(it) })
        Subtasks(
            subtasks = subtasks,
            addNewFun = addNewSubtaskFun,
            onTitleChanged = updateSubtaskTitle
        )
        SaveButton {
            saveFun()
        }
    }
}

@Composable
private fun SaveButton(saveFun: () -> Unit) {
    Button(onClick = {
        saveFun()
    }) {
        Text("Save")
    }
}

@Preview
@Composable
private fun TaskForm_Preview() {
    MaterialTheme {
        TaskForm(
            title = "",
            subtasks = listOf(),
            addNewSubtaskFun = {},
            onTitleChanged = {},
            updateSubtaskTitle = { _, _ -> },
            saveFun = {}
        )
    }
}