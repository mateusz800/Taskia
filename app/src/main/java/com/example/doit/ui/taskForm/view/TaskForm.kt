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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TaskForm(viewModel: TaskFormViewModel, closeFunc: () -> Unit) {
    val title by viewModel.title.collectAsState()
    Column(modifier = Modifier.padding(20.dp)) {
        TextField(value = title, onValueChange = viewModel::onTitleChanged)
        Button(onClick = {
            viewModel.saveTask()
            closeFunc()
        }) {
            Text("Save")
        }
    }
}


@Preview
@Composable
private fun TaskForm_Preview() {
    MaterialTheme {
        TaskForm(hiltViewModel()) {}
    }
}