package com.mabn.taskia.ui.taskForm.dateTime

import androidx.compose.foundation.layout.Row
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mabn.taskia.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun TaskDateTime(
    viewModel: TaskDateVmInterface
) {
    val dayLabel by viewModel.dueDay.collectAsState()
    val timeLabel by viewModel.startTime.collectAsState(stringResource(id = R.string.no_time))

    TaskDateTime(
        updateDate = { value -> viewModel.updateDueToDate(value) },
        updateTime = { value -> viewModel.updateStartTime(value) },
        dayLabel = dayLabel,
        timeLabel = timeLabel
    )
}

@Composable
private fun TaskDateTime(
    updateDate: (String) -> Unit,
    updateTime: (String) -> Unit,
    dayLabel: String,
    timeLabel: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TaskDate(updateDate = updateDate, dayLabel = dayLabel)
        TaskTime(updateTime = updateTime, timeLabel = timeLabel)
    }
}

@Preview
@Composable
private fun TaskDateTimePreview() {
    MaterialTheme {
        Surface {
            TaskDateTime(updateDate = {}, updateTime = {}, dayLabel = "Today", timeLabel = "10:30")

        }
    }
}




