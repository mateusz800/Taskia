package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    task: Task,
    subtasks: List<Task>?,
    removeItemFunc: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Unit,
    isSubtask: Boolean = false,
    onClick: ((task: Task) -> Unit)?
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToEnd) {
            removeItemFunc(task)
        }
        false
    })
    val onClickFun: (() -> Unit)? = if (onClick != null) {
        { onClick.invoke(task) }
    } else null
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(top =  0.dp)
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .clip(RectangleShape)
                        .background(Color.Red),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.remove),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            },
            directions = setOf(DismissDirection.StartToEnd)
        ) {

            TaskGeneralInfo(
                task.status,
                task.title,
                dueDay = if (task.endDate != null && task.endDate!!.isBefore(
                        LocalDate.now().atStartOfDay()
                    )
                )
                    task.getEndDay(context = LocalContext.current) else null,
                onCheck = {
                    toggleStatusFun(task)
                },
                onClick = onClickFun
            )
        }
        if (!subtasks.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
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
    onClick: (() -> Unit)?
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(status) {
                onCheck()
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = title,
                modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (status) TextDecoration.LineThrough else TextDecoration.None
            )
        }
        if (dueDay?.isNotEmpty() == true) {
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .padding(horizontal = 50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = stringResource(id = R.string.deadline),
                    modifier = Modifier
                        .height(16.dp)
                        .padding(end = 10.dp),
                    tint = MaterialTheme.colors.error
                )
                Text(dueDay, fontSize = 12.sp, color = MaterialTheme.colors.error)
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
                    isSubtask = true,
                    onClick = null
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