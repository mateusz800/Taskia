package com.mabn.taskia.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.startEditFormActivity
import com.mabn.taskia.ui.taskList.ListEvent
import com.mabn.taskia.ui.taskList.TaskListViewModel
import com.mabn.taskia.ui.taskList.components.TaskListSection

@Composable
fun CalendarView(
    calendarViewModel: CalendarViewModel,
    taskListViewModel: TaskListViewModel
) {
    val tasks = calendarViewModel.tasks.observeAsState(listOf())
    val context = LocalContext.current

    val grouped = tasks.value.groupBy {
        if (it.first.endDate != null) 0 else 1
    }

    DisposableEffect(Unit) {
        onDispose {
            calendarViewModel.refreshTasks()
        }
    }

    Column {
        CalendarDatePicker(onDateChange = { date ->
            calendarViewModel.onEvent(
                CalendarEvent.DateChanged(
                    date
                )
            )
        })
        grouped.keys.forEach {
            if (it == 1) {
                Text(
                    stringResource(id = R.string.future), style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(top = 15.dp)
                )
            }
            if (grouped[it] != null) {
                TaskListSection(
                    items = grouped[it]!!,
                    onTaskRemove = { task -> taskListViewModel.onEvent(ListEvent.TaskRemoved(task)) },
                    toggleStatusFun = { task ->
                        taskListViewModel.onEvent(
                            ListEvent.TaskStatusChanged(
                                task
                            )
                        )
                    },
                    onItemClick = {
                        startEditFormActivity(context, it)
                    })
            }
        }

    }
}