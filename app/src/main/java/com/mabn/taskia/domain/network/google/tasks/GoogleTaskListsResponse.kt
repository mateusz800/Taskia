package com.mabn.taskia.domain.network.google.tasks

data class GoogleTaskListsResponse(
    val nextSyncToken:String,
    val items: List<CalendarItem>
)

data class CalendarItem(
    val id:String
)