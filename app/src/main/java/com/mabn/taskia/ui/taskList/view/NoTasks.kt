package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.ui.taskList.ListType

@Composable
fun NoTasks(listType: ListType = ListType.Unscheduled) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        Text(
            stringResource(
                id = when (listType) {
                    is ListType.Today -> R.string.no_today_tasks
                    else -> R.string.no_tasks
                }
            ),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center
        )
        Text(
            stringResource(
                id = when (listType) {
                    is ListType.Today -> R.string.relax_or_add
                    is ListType.Completed -> R.string.complete_to_show
                    else -> R.string.click_to_add
                }
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun NoTasks_Preview() {
    MaterialTheme {
        NoTasks()
    }
}