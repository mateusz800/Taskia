package com.example.doit.ui.taskList

import com.example.doit.R

sealed class ListType(val textId: Int) {
    object Today : ListType(R.string.today_tasks)
    object Upcoming : ListType(R.string.upcoming_tasks)
    object Unscheduled : ListType(R.string.unscheduled_tasks)
    object Completed : ListType(R.string.completed_tasks)
}