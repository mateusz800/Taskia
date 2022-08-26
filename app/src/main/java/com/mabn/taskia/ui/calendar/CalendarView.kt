package com.mabn.taskia.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.mabn.taskia.ui.taskList.ListType
import com.mabn.taskia.ui.taskList.TaskListViewModel
import com.mabn.taskia.ui.taskList.components.TaskListSection

@Composable
fun CalendarView(
    calendarViewModel: CalendarViewModel,
    taskListViewModel: TaskListViewModel
) {
    val tasks = calendarViewModel.tasks.observeAsState(listOf())
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val grouped = tasks.value.groupBy {
        if (it.first.completionTime != null) ListType.CompletedThatDay
        else if (it.first.endDate != null) ListType.Calendar
        else ListType.Unscheduled
    }

    DisposableEffect(Unit) {
        onDispose {
            calendarViewModel.refreshTasks()
        }
    }

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        CalendarDatePicker(onDateChange = { date ->
            calendarViewModel.onEvent(
                CalendarEvent.DateChanged(
                    date
                )
            )
        }, modifier = Modifier.padding(top = 20.dp))
        grouped.keys.sortedBy { if (it is ListType.CompletedThatDay) -2 else if (it is ListType.Calendar) -1 else 1 }
            .forEach {
                if (grouped[it] != null) {
                    if (it == ListType.CompletedThatDay) {
                        Text(
                            stringResource(id = R.string.completed_that_day),
                            style = MaterialTheme.typography.h2,
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .padding(top = 15.dp)
                        )
                    } else if (it == ListType.Unscheduled) {
                        Text(
                            stringResource(id = R.string.unscheduled_tasks),
                            style = MaterialTheme.typography.h2,
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .padding(top = 15.dp)
                        )
                    }
                    TaskListSection(
                        items = grouped[it]!!,
                        onTaskRemove = { task ->
                            taskListViewModel.onEvent(
                                ListEvent.TaskRemoved(
                                    task
                                )
                            )
                        },
                        toggleStatusFun = { task ->
                            taskListViewModel.onEvent(
                                ListEvent.TaskStatusChanged(
                                    task
                                )
                            )
                        },
                        showDates = false,
                        onItemClick = { task ->
                            startEditFormActivity(context, task)
                        })
                }
            }
    }
}