package com.mabn.taskia.ui.taskForm.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Label(text:String){
    Text(
        text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 20.dp)
    )
}