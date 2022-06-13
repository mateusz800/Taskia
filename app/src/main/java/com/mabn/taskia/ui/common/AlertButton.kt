package com.mabn.taskia.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AlertButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = modifier
        .padding(10.dp)) {
        Text(text)
    }
}

@Preview
@Composable
private fun AlertButton_Preview(){
    MaterialTheme{
        AlertButton(text = "save", onClick = {})
    }
}