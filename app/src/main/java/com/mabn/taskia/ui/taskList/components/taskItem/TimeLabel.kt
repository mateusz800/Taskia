package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeLabel(time: String) {
    Text(
        time,
        color = MaterialTheme.colors.primary,
        modifier = Modifier.requiredWidth(50.dp)
    )
}