package com.mabn.taskia.ui.taskForm.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.common.CustomTextField
import com.mabn.taskia.ui.taskForm.TaskFormViewModel

@Composable
fun TaskForm(viewModel: TaskFormViewModel, closeFunc: () -> Unit) {
    val title by viewModel.title.collectAsState()
    val dueToDayText by viewModel.dueDay.collectAsState()
    val isVisible = viewModel.isVisible.collectAsState()
    val subtasks = viewModel.subtasks

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 20.dp)
                .width(50.dp)
                .height(5.dp)
                .background(
                    MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
                    RoundedCornerShape(20.dp),
                )
        )
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
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .testTag("task_form")
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        ) {
            TitleTextField(
                value = title,
                onValueChanged = onTitleChanged,
                isVisible = isVisible,
                onDoneKeyClick = saveFun,
            )
            SaveButton(enabled = title.isNotBlank()) {
                keyboardController?.hide()
                saveFun()
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            DueTo(
                dayLabel = endDateText,
                updateDate = updateDueTo
            )
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
    isVisible: Boolean = true,
) {
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(isVisible) {
        if (isVisible && value.isBlank()) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }

    }
    CustomTextField(
        value = value,
        onValueChange = onValueChanged,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, start = 5.dp, top = 10.dp)
            .focusRequester(focusRequester)
            .testTag("title_input")
            .onKeyEvent {
                if (it.key == Key.Enter && value.isNotBlank()) {
                    onDoneKeyClick.invoke()
                    keyboardController?.hide()
                }
                true
            },

        placeholderText = stringResource(id = R.string.task_name),
        underline = true,
        keyboardActions = KeyboardActions(onDone = {
            onDoneKeyClick()
            keyboardController?.hide()
        }, onPrevious = {
            println("previous")
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
        Icon(
            Icons.Default.Save,
            contentDescription = stringResource(id = R.string.save),
            tint = MaterialTheme.colors.secondary
        )
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