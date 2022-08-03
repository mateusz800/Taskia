package com.mabn.taskia.ui.taskForm.components.subtasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.common.AddNewButton
import com.mabn.taskia.ui.taskForm.FormEvent
import com.mabn.taskia.ui.taskForm.components.Label


@Composable
fun Subtasks(subtasks: List<Task>?, onEvent: (FormEvent) -> Unit) {
    Subtasks(
        subtasks = subtasks,
        addNewFun = { onEvent(FormEvent.AddNewSubtask) },
        onTitleChanged = { subtask, newTitle ->
            onEvent(
                FormEvent.SubtaskTitleChanged(
                    subtask,
                    newTitle
                )
            )
        }
    )
}

@Composable
private fun Subtasks(
    subtasks: List<Task>?,
    addNewFun: () -> Unit,
    onTitleChanged: (Task, String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Label(stringResource(id = R.string.subtasks))
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

@Preview
@Composable
private fun Subtasks_Preview() {
    MaterialTheme {
        Surface {
            Subtasks(
                subtasks = listOf(Task(title = "Bread"), Task(title = "Eggs")),
                onEvent = {}
            )
        }
    }
}