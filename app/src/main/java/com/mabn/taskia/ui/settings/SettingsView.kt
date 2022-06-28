package com.mabn.taskia.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.ui.theme.DoItTheme

@Composable
fun SettingsView(startConnectedAccountsActivity: () -> Unit) {
    DoItTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                SettingsButton(text = stringResource(id = R.string.connected_accounts), onClick = {
                    startConnectedAccountsActivity()
                })
                Divider()
            }
        }
    }
}

@Composable
fun SettingsButton(
    text: String,
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
        Text(
            text = text,
            color = MaterialTheme.colors.onBackground.copy(alpha = if (enabled) 1f else 0.5f)
        )
    }

}

