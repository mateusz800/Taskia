package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mabn.taskia.R

@Composable
fun DueDayLabel(value: String, color: Color) {
    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .padding(horizontal = 50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Default.CalendarToday,
            contentDescription = stringResource(id = R.string.deadline),
            modifier = Modifier
                .height(16.dp)
                .padding(end = 10.dp),
            tint = color
        )
        Text(value, fontSize = 12.sp, color = color)
    }
}

@Preview
@Composable
private fun DueDay_Preview() {
    MaterialTheme {
        Surface {
            DueDayLabel(value = "Yesterday", color = MaterialTheme.colors.error)
        }
    }
}
