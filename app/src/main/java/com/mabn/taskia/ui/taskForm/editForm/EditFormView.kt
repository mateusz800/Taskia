package com.mabn.taskia.ui.taskForm.editForm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.mabn.taskia.ui.taskForm.FormEvent
import com.mabn.taskia.ui.taskForm.FormState
import com.mabn.taskia.ui.taskForm.TaskFormViewModel
import com.mabn.taskia.ui.taskForm.components.SaveButton
import com.mabn.taskia.ui.taskForm.components.TitleTextField
import com.mabn.taskia.ui.taskForm.components.dateTime.TaskDateTime
import com.mabn.taskia.ui.taskForm.components.subtasks.Subtasks
import com.mabn.taskia.ui.taskForm.components.tags.Tags

@Composable
fun EditFormView(viewModel: TaskFormViewModel, goBack: () -> Unit) {
    val formState = viewModel.formState.observeAsState()
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        EditFormView(
            formState = formState.value!!,
            onEvent = viewModel::onEvent,
            validate = viewModel::verifyData
        ) {
            goBack()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EditFormView(
    formState: FormState,
    onEvent: (FormEvent) -> Unit,
    validate: () -> Boolean,
    close: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TitleTextField(
        value = formState.title,
        onValueChanged = { onEvent(FormEvent.TitleChanged(it)) },
        isVisible = true,
        onDoneKeyClick = {
            if (validate()) {
                onEvent(FormEvent.Submit)
                close()
            } else {
                keyboardController?.hide()
            }
        },
    )
    TaskDateTime(
        onEvent = onEvent,
        dayLabel = formState.dayLabel,
        timeLabel = formState.timeLabel
    )
    Tags(
        tags = formState.tags,
        onEvent = onEvent,
    )
    Subtasks(
        onEvent = onEvent,
        subtasks = formState.subtasks
    )
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SaveButton(enabled = validate(), withText = true) {
            keyboardController?.hide()
            onEvent(FormEvent.Submit)
            close()
        }
    }

}
