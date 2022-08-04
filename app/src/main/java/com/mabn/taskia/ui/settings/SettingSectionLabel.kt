package com.mabn.taskia.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSectionLabel(text: String) {
    Box(Modifier.padding(start = 20.dp, top = 10.dp)) {
        Text(text, modifier = Modifier.alpha(0.7f))
    }
}

@Preview
@Composable
private fun SettingsSectionLabel_Preview() {
    MaterialTheme {
        Surface {
            SettingsSectionLabel(text = "About")
        }
    }
}