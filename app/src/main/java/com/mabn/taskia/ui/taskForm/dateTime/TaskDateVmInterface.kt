package com.mabn.taskia.ui.taskForm.dateTime

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TaskDateVmInterface {
    val dueDay: StateFlow<String>
    val startTime: Flow<String>
    fun updateDueToDate(value: String)
    fun updateStartTime(value:String)
}