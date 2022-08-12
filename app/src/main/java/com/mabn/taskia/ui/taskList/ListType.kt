package com.mabn.taskia.ui.taskList

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.mabn.taskia.R

sealed class ListType(val textId: Int, val icon: ImageVector? = null) {
    object Tasks : ListType(R.string.tasks, icon = Icons.Default.List)
    object Calendar : ListType(R.string.calendar, icon = Icons.Default.CalendarMonth)
    object Loading: ListType(R.string.emptyString)
    object Unscheduled: ListType(R.string.unscheduled_tasks)
}