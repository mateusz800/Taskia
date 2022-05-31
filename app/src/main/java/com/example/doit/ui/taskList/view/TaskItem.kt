package com.example.doit.ui.taskList.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doit.R
import com.example.doit.domain.model.Task

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    task: Task,
    subtasks: List<Task>?,
    removeItemFunc: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Unit,
    onClick: (task: Task) -> Unit
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToEnd) {
            removeItemFunc(task)
        }
        false
    })
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        SwipeToDismiss(state = dismissState, background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RectangleShape)
                    .background(Color.Red)
            )

        }) {

            TaskGeneralInfo(
                task.status,
                task.title,
                dueDay = task.getEndDay(context = LocalContext.current),
                onCheck = {
                    toggleStatusFun(task)
                },
                onClick = {
                    onClick(task)
                }
            )
        }
        if (!subtasks.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
                    .padding(vertical = 10.dp)
            ) {
                SubtasksList(
                    subtasks = subtasks,
                    removeItemFunc = removeItemFunc,
                    toggleStatusFun = toggleStatusFun
                )
            }
        }
    }
}

@Composable
fun TaskGeneralInfo(
    status: Boolean,
    title: String,
    dueDay: String? = null,
    onCheck: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(30.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(status) {
                onCheck()
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = title,
                modifier = Modifier.clickable { onClick() },
                fontSize = 18.sp,
                textDecoration = if (status) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(horizontal = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (dueDay?.isNotEmpty() == true) {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = stringResource(id = R.string.deadline),
                    modifier = Modifier
                        .height(16.dp)
                        .padding(end = 10.dp)
                )
                Text(dueDay, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SubtasksList(
    subtasks: List<Task>,
    removeItemFunc: (Task) -> Unit,
    toggleStatusFun: (Task) -> Unit
) {
    Column {
        subtasks.forEach { subtask ->
            key(subtask.id) {
                TaskItem(
                    task = subtask,
                    subtasks = null,
                    removeItemFunc = { removeItemFunc(subtask) },
                    toggleStatusFun = { toggleStatusFun(subtask) },
                    onClick = {/* do nothing */ }
                )
            }
        }
    }
}

@Composable
fun CustomCheckbox(status: Boolean, enabled: Boolean = true, onCheck: () -> Unit) {
    Checkbox(
        checked = status,
        enabled = enabled,
        onCheckedChange = {
            onCheck()
        }
    )
}

@Preview
@Composable
private fun TaskItem_Preview() {
    val task = Task(title = "Shopping")
    val subtasks = listOf(Task(title = "Bread"))
    MaterialTheme {
        TaskItem(
            task = task,
            subtasks = subtasks,
            removeItemFunc = {},
            toggleStatusFun = {},
            onClick = {}
        )
    }
}