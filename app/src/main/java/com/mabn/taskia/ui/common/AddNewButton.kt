package com.mabn.taskia.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddNewButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        elevation = null,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(Icons.Rounded.Add, contentDescription = null)
        Spacer(Modifier.width(10.dp))
        Text(text)
    }
}

@Preview
@Composable
private fun AddNewButton_Preview() {
    MaterialTheme {
        Surface {
            AddNewButton(text = "Add new subtask") {

            }
        }
    }
}