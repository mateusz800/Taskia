package com.example.doit.ui.taskForm.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doit.R
import com.example.doit.domain.model.Task
import com.example.doit.ui.taskForm.TaskFormViewModel
import java.util.*

@Composable
fun TaskForm(viewModel: TaskFormViewModel, closeFunc: () -> Unit) {
    val title by viewModel.title.collectAsState()
    val dueToDayText by viewModel.dueDay.collectAsState()
    val isVisible = viewModel.isVisible.collectAsState()
    val subtasks = viewModel.subtasks

    TaskForm(
        isVisible = isVisible.value,
        title = title,
        subtasks = subtasks,
        onTitleChanged = viewModel::onTitleChanged,
        updateSubtaskTitle = viewModel::updateSubtaskTitle,
        addNewSubtaskFun = viewModel::addNewSubtask,
        saveFun = {
            if (viewModel.verifyData()) {
                viewModel.saveTask()
                viewModel.clear()
                closeFunc()
            }
        },
        endDateText = dueToDayText,
        updateDueTo = { value -> viewModel.updateDueToDate(value) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TaskForm(
    title: String,
    subtasks: List<Task>,
    onTitleChanged: (String) -> Unit,
    updateSubtaskTitle: (Task, String) -> Unit,
    addNewSubtaskFun: () -> Unit,
    saveFun: () -> Unit,
    isVisible: Boolean = false,
    endDateText: String,
    updateDueTo: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .testTag("task_form")
    ) {
        TitleTextField(
            value = title,
            onValueChanged = onTitleChanged,
            isVisible = isVisible,
            onDoneKeyClick = saveFun
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            DueTo(
                dayLabel = endDateText,
                updateDate = updateDueTo
            )
            SaveButton(enabled = title.isNotBlank()) {
                keyboardController?.hide()
                saveFun()
            }
        }

        Subtasks(
            subtasks = subtasks,
            addNewFun = addNewSubtaskFun,
            onTitleChanged = updateSubtaskTitle
        )


    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TitleTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    onDoneKeyClick: () -> Unit,
    isVisible: Boolean = true
) {
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(isVisible) {
        if (isVisible && value.isBlank()) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }

    }
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
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .testTag("title_input")
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onDoneKeyClick.invoke()
                    keyboardController?.hide()
                }
                true
            },
        placeholder = { Text(stringResource(id = R.string.task_name)) },
        keyboardActions = KeyboardActions(onDone = {
            onDoneKeyClick()
            keyboardController?.hide()
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SaveButton(enabled: Boolean = true, saveFun: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    IconButton(
        modifier = Modifier.testTag("save_task_button"),
        onClick = {
            saveFun()
            keyboardController?.hide()
        },
        enabled = enabled
    ) {
        Icon(Icons.Default.Send, contentDescription = stringResource(id = R.string.save))
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
            saveFun = {},
            endDateText = "Today",
            updateDueTo = { }
        )
    }
}