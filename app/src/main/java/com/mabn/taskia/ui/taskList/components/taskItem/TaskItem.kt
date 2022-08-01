package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                SubtaskList(
                    subtasks = subtasks,
                    removeItemFunc = removeItemFunc,
                    toggleStatusFun = toggleStatusFun
                )
            }
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