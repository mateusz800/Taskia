package com.mabn.taskia.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
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
        TaskListSection(
            items = tasks.value,
            onTaskRemove = { task -> taskListViewModel.onEvent(ListEvent.TaskRemoved(task)) },
            toggleStatusFun = { task -> taskListViewModel.onEvent(ListEvent.TaskStatusChanged(task)) },
            onItemClick = {
                startEditFormActivity(context, it)
            })
    }
}