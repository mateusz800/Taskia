package com.mabn.taskia.ui.taskForm.dateTime

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskTime(updateTime: (String) -> Unit, timeLabel: String) {
    val context = LocalContext.current
    val time = remember { mutableStateOf("") }
    val menuExpanded = remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    val timePickerDialog = TimePickerDialog(
        context, { _, mHour: Int, mMinute: Int ->
            time.value = "${"%02d".format(mHour)}:${"%02d".format(mMinute)}"
            updateTime(time.value)
        }, hour, minute, true
    )
    Picker(label = timeLabel,
        icon = {
            Icon(
                Icons.Default.Timer,
                contentDescription = stringResource(id = R.string.deadline),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .height(14.dp)
            )
        }, menuItem = mapOf(Pair(stringResource(id = R.string.pick_time)) {
            timePickerDialog.show()
            menuExpanded.value = false
        }, Pair(stringResource(id = R.string.reset_time)) {
            time.value = context.getString(R.string.no_time)
            updateTime(time.value)
            menuExpanded.value = false
        })
    )
}

@Preview
@Composable
private fun TaskTime_Preview() {
    MaterialTheme {
        Surface {
            TaskTime(updateTime = {}, timeLabel = "09:30")
        }
    }
}