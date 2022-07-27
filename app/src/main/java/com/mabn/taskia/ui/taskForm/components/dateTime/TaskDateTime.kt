package com.mabn.taskia.ui.taskForm.components.dateTime

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.mabn.taskia.ui.taskForm.FormEvent


@Composable
fun TaskDateTime(
    onEvent: (FormEvent) -> Unit,
    dayLabel: String,
    timeLabel: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TaskDate(
            updateDate = { value -> onEvent(FormEvent.TaskDateChanged(value)) },
            dayLabel = dayLabel
        )
        TaskTime(
            updateTime = { value -> onEvent(FormEvent.TaskTimeChanged(value)) },
            timeLabel = timeLabel
        )
    }
}

@Preview
@Composable
private fun TaskDateTimePreview() {
    MaterialTheme {
        Surface {
            TaskDateTime(onEvent = {}, dayLabel = "Today", timeLabel = "10:30")

        }
    }
}




