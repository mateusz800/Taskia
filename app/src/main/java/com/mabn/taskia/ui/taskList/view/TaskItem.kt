package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mabn.taskia.R
import com.mabn.taskia.domain.model.Task
import com.mabn.taskia.ui.common.DeleteBackground
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    task: Task,
    subtasks: List<Task>?,
    removeItemFunc: (task: Task) -> Unit,
    toggleStatusFun: (task: Task) -> Boolean,
    isSubtask: Boolean = false,
    onClick: ((task: Task) -> Unit)?
) {
    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToEnd) {
            removeItemFunc(task)
        }
        false
    })
    val isExpanded = remember { mutableStateOf(false) }
    val onClickFun: (() -> Unit)? = if (onClick != null) {
        { onClick.invoke(task) }
    } else null
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(top = 0.dp)
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                DeleteBackground()
            },
            directions = setOf(DismissDirection.StartToEnd)
        ) {

            TaskGeneralInfo(
                task.status,
                task.title,
                startTime = if (task.startTime != null) task.startTime.toString() else null,
                dueDay = if (task.endDate != null && task.endDate!!.isBefore(
                        LocalDate.now().atStartOfDay()
                    )
                )
                    task.getEndDay(context = LocalContext.current) else null,
                onCheck = {
                    toggleStatusFun(task)
                },
                onClick = onClickFun,
                expandStatus = isExpanded.value,
                expandFun = if (subtasks.isNullOrEmpty()) {
                    null
                } else {
                    {
                        isExpanded.value = !isExpanded.value
                    }
                }
            )
        }
        if (isExpanded.value && !subtasks.isNullOrEmpty()) {
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
    startTime: String? = null,
    onCheck: () -> Boolean,
    onClick: (() -> Unit)?,
    expandStatus: Boolean = false,
    expandFun: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                if (startTime != null) {
                    Text(
                        startTime, color = MaterialTheme.colors.primary,
                        modifier = Modifier.requiredWidth(50.dp)
                    )
                }
                if (expandFun != null) {
                    Spacer(Modifier.width(10.dp))
                    IconButton(
                        modifier = Modifier.requiredWidth(20.dp),
                        onClick = { expandFun.invoke() }) {
                        Icon(
                            if (expandStatus) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            null
                        )
                    }
                }
            }

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
    toggleStatusFun: (Task) -> Boolean
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
fun CustomCheckbox(status: Boolean, enabled: Boolean = true, onCheck: () -> Boolean) {
    val checked = remember { mutableStateOf(status) }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(30.dp)
            .padding(vertical = 10.dp)
            .padding(end = 10.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(enabled = enabled) {
                if (onCheck()) {
                    checked.value = !checked.value
                }
            }
    ) {
        if (checked.value) {
            Icon(
                Icons.Default.Check, status.toString(),
                tint = MaterialTheme.colors.primary
            )
        }
    }
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
            toggleStatusFun = { true },
            onClick = {}
        )
    }
}