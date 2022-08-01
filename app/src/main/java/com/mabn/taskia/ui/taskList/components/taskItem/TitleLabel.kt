package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun TitleLabel(title: String, isCompleted: Boolean = false, onClick: () -> Unit) {
    Text(
        text = title,
        modifier = Modifier.clickable { onClick() },
        style = MaterialTheme.typography.subtitle1,
        overflow = TextOverflow.Ellipsis,
        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
    )
}