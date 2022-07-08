package com.mabn.taskia.ui.taskList

import com.mabn.taskia.R

sealed class ListType(val textId: Int) {
    object Today : ListType(R.string.today_tasks)
    object Upcoming : ListType(R.string.upcoming_tasks)
    object Unscheduled : ListType(R.string.unscheduled_tasks)
    object Completed : ListType(R.string.completed_tasks)
    object Loading : ListType(R.string.loading)
}