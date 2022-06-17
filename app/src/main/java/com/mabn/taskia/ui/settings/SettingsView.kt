package com.mabn.taskia.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mabn.taskia.ui.theme.DoItTheme

@Composable
fun SettingsView(googleCalendarSignIn: () -> Unit) {
    DoItTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column{
                Button(onClick = { googleCalendarSignIn() }) {
                    Text("Google Calendar")
                }
            }
        }
    }
}

