package com.mabn.taskia.ui.taskForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.ui.taskForm.components.SaveButton
import com.mabn.taskia.ui.taskForm.components.TitleTextField
import com.mabn.taskia.ui.taskForm.components.dateTime.TaskDateTime
import com.mabn.taskia.ui.taskForm.components.subtasks.Subtasks
import com.mabn.taskia.ui.taskForm.components.tags.Tags

@Composable
fun TaskForm(
    viewModel: TaskFormViewModel,
    closeFunc: () -> Unit
) {
    val isVisible = viewModel.isVisible.observeAsState(false)
    val formState = viewModel.formState.observeAsState(FormState())

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
            validate = viewModel::verifyData,
            close = closeFunc,
            onEvent = viewModel::onEvent,
            formState = formState.value
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TaskForm(
    validate: () -> Boolean,
    close: () -> Unit,
    onEvent: (FormEvent) -> Unit,
    isVisible: Boolean = false,
    formState: FormState
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
                value = formState.title,
                onValueChanged = { onEvent(FormEvent.TitleChanged(it)) },
                isVisible = isVisible,
                onDoneKeyClick = {
                    if (validate()) {
                        onEvent(FormEvent.Submit)
                        close()
                    } else {
                        keyboardController?.hide()
                    }
                },
            )
            SaveButton(enabled = validate()) {
                keyboardController?.hide()
                onEvent(FormEvent.Submit)
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TaskDateTime(
                onEvent = onEvent,
                dayLabel = formState.dayLabel,
                timeLabel = formState.timeLabel
            )
        }
        Tags(
            tags = formState.tags,
            onEvent = onEvent,
            isVisible = isVisible
        )

        Subtasks(
            onEvent = onEvent,
            subtasks = formState.subtasks
        )

    }
}

@Preview
@Composable
private fun TaskForm_Preview() {
    MaterialTheme {
        Surface {
            TaskForm(
                validate = { true },
                close = { },
                onEvent = {},
                formState = FormState()
            )
        }
    }
}




