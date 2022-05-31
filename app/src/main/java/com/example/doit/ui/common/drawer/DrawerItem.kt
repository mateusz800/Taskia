package com.example.doit.ui.common.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DrawerItem(text: String, active: Boolean = false, onClick: () -> Unit) {
    Button(
        elevation = null,
        colors = ButtonDefaults.buttonColors(backgroundColor = if(active) Color.LightGray else Color.Transparent),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick() }) {
        Text(text)
    }
}

@Preview
@Composable
private fun DrawerItem_Preview() {
    MaterialTheme {
        DrawerItem(text = "Today") {

        }
    }
}