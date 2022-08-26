package com.mabn.taskia.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsButton(
    text: String,
    description: String? = null,
    onClick: () -> Unit,
    icon: Painter? = null,
    enabled: Boolean = true
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable(enabled = enabled) { onClick() }
        .padding(20.dp)) {
        if (icon != null) {
            Icon(
                icon,
                null,
                tint = MaterialTheme.colors.onBackground.copy(alpha = if (enabled) 1f else 0.5f),
                modifier = Modifier
                    .height(30.dp)
                    .padding(end = 10.dp)
            )
        }
        Column {
            Text(
                text = text,
                color = MaterialTheme.colors.onBackground.copy(alpha = if (enabled) 1f else 0.5f)
            )
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    fontSize = 10.sp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = if (enabled) 1f else 0.5f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsButton_Preview() {
    MaterialTheme {
        Surface {
            SettingsButton(text = "Connected Accounts", onClick = { })
        }
    }
}