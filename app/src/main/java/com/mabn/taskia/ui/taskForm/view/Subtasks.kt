package com.mabn.taskia.ui.taskForm.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.common.AddNewButton
import com.mabn.taskia.ui.common.CustomTextField
import com.mabn.taskia.ui.taskList.view.CustomCheckbox

@Composable
fun Subtasks(
    subtasks: List<Task>?,
    addNewFun: () -> Unit,
    onTitleChanged: (Task, String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Text(
            stringResource(id = R.string.subtasks),
            fontWeight = FontWeight.Bold
        )
        Column(Modifier.padding(start = 20.dp)) {
            subtasks?.forEachIndexed { index, task ->
                SubtaskInput(
                    task = task,
                    onTitleChanged = onTitleChanged,
                    onEnter = addNewFun,
                    focus = index == subtasks.size - 1 && task.title.isEmpty()
                )
            }
            AddNewButton(
                text = stringResource(id = R.string.add_subtask),
                onClick = addNewFun
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SubtaskInput(
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
            onTitleChanged = { _, _ -> }
        )
    }
}

