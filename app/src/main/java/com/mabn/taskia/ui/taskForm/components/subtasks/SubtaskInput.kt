package com.mabn.taskia.ui.taskForm.components.subtasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.common.CustomTextField
import com.mabn.taskia.ui.taskList.view.CustomCheckbox


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubtaskInput(
    task: Task,
    onTitleChanged: (Task, String) -> Unit,
    onEnter: () -> Unit,
    focus: Boolean = false
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(focus) {
        if (focus) {
            focusRequester.requestFocus()
        }
    }


    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomCheckbox(status = task.status, enabled = false) {
            true
        }
        Spacer(modifier = Modifier.width(10.dp))
        CustomTextField(
            value = task.title,
            onValueChange = { onTitleChanged(task, it) },
            style = MaterialTheme.typography.body2,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onEnter.invoke() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .focusRequester(focusRequester)
                .onKeyEvent {
                    if (it.key == Key.Enter) {
                        onEnter.invoke()
                    }
                    true
                },
        )
    }
}

@Preview
@Composable
private fun SubtaskInput_Preview() {
    MaterialTheme {
        Surface {
            SubtaskInput(
                task = Task(title = "Shopping"),
                onTitleChanged = { _, _ -> },
                onEnter = { })
        }
    }
}

@Preview
@Composable
private fun SubtaskInput_Empty_Preview() {
    MaterialTheme {
        Surface {
            SubtaskInput(
                task = Task(title = ""),
                onTitleChanged = { _, _ -> },
                onEnter = { })
        }
    }
}