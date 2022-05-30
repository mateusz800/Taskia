package com.example.doit.ui.taskForm.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.doit.R
import java.util.*

@Composable
fun DueTo(
    dayLabel: String,
    timeLabel: String,
    updateDate: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    calendar.time = Date()

    val date = remember { mutableStateOf("$year-${"%02d".format(month + 1)}-${"%02d".format(day)}") }
    val time = remember { mutableStateOf("00:00:00") }

    val datePickerDialog = DatePickerDialog(
        context, { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            date.value = "$mYear-${"%02d".format(mMonth + 1)}-${"%02d".format(mDayOfMonth)}"
            updateDate("${date.value}T${time.value}")
        }, year, month, day
    )
    val timePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            time.value = "$mHour:$mMinute:00"
            updateDate("${date.value}T${time.value}")
        }, hour, minute, false
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            stringResource(id = R.string.due_to),
            fontWeight = FontWeight.Bold
        )
        Button(
            elevation = null,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            onClick = { datePickerDialog.show() }) {
            Text(dayLabel)
        }
        Button(
            elevation = null,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            onClick = { timePickerDialog.show() }) {
            Text(timeLabel)
        }
    }

}