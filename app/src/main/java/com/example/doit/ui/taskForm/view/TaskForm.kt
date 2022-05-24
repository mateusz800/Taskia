package com.example.doit.ui.taskForm.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doit.R
import com.example.doit.domain.model.Task
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
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        TitleTextField(value = title, onValueChanged = onTitleChanged)
        Subtasks(
            subtasks = subtasks,
            addNewFun = addNewSubtaskFun,
            onTitleChanged = updateSubtaskTitle
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            SaveButton {
                saveFun()
            }
        }


    }
}

@Composable
private fun TitleTextField(value: String, onValueChanged: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        singleLine = true,
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.task_name)) }
    )
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