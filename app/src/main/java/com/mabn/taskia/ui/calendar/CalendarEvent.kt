package com.mabn.taskia.ui.calendar

import com.mabn.taskia.domain.model.Tag
import java.time.LocalDate

sealed class CalendarEvent{
    data class DateChanged(val date: LocalDate):CalendarEvent()
    data class FilterTagsChanged(val tags: List<Tag>) : CalendarEvent()

}
