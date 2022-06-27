package com.mabn.taskia.domain.network.google.calendar

data class GoogleCalendarListResponse(
    val nextSyncToken:String,
    val items: List<CalendarItem>
)

data class CalendarItem(
    val id:String
)