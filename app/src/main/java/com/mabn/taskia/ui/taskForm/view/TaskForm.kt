package com.mabn.taskia.ui.taskForm.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Tag
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.common.CustomTextField
import com.mabn.taskia.ui.taskForm.TaskFormViewModel
import kotlinx.coroutines.delay

@Composable
fun TaskForm(
    viewModel: TaskFormViewModel,
    closeFunc: () -> Unit
) {
    val title by viewModel.title.collectAsState()
    val dueToDayText by viewModel.dueDay.collectAsState()
    val isVisible = viewModel.isVisible.collectAsState()
    val subtasks = viewModel.subtasks
    val tags = viewModel.tags

    //val showAlert = remember { mutableStateOf(showNotSavedAlert) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
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
            tags = tags,
            onTitleChanged = viewModel::onTitleChanged,
            updateSubtaskTitle = viewModel::updateSubtaskTitle,
            updateTagValue = viewModel::updateTagValue,
            addNewSubtaskFun = viewModel::addNewSubtask,
            addNewTagFun = viewModel::addNewTag,
            saveFun = {
                if (viewModel.verifyData()) {
                    viewModel.saveTask()
                    closeFunc()
                }
            },
            endDateText = dueToDayText,
            updateDueTo = { value -> viewModel.updateDueToDate(value) }
        )
        /*
        NotSavedAlert(show = showAlert.value, saveFun = {
            if (viewModel.verifyData()) {
                viewModel.saveTask()
                viewModel.clear()
                closeFunc()
            }
        }) {
            showAlert.value = false
            closeFunc()
        }

         */
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TaskForm(
    title: String,
    subtasks: List<Task>,
    tags: List<Tag>,
    onTitleChanged: (String) -> Unit,
    updateSubtaskTitle: (Task, String) -> Unit,
    updateTagValue: (Tag, String) -> Unit,
    addNewSubtaskFun: () -> Unit,
    addNewTagFun: (Boolean) -> Unit,
    saveFun: () -> Unit,
    isVisible: Boolean = false,
    endDateText: String,
    updateDueTo: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
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
        Tags(
            tags = tags,
            addNewFun = addNewTagFun,
            onTitleChanged = updateTagValue
        )

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
    val focusRequester = remember{FocusRequester()}
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(isVisible) {
        if (isVisible && value.isBlank()) {
            keyboardController?.show()
            delay(10)
            focusRequester.requestFocus()
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
            tint = MaterialTheme.colors.onBackground
        )
    }
}

