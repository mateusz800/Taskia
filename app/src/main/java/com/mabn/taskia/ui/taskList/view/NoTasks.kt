package com.mabn.taskia.ui.taskList.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mabn.taskia.R

@Composable
fun NoTasks(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            stringResource(id = R.string.no_tasks),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(stringResource(id = R.string.click_to_add))
    }
}

@Preview
@Composable
private fun NoTasks_Preview(){
    MaterialTheme{
        NoTasks()
    }
}