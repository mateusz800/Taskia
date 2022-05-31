package com.example.doit.ui.taskForm.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doit.R
import java.util.*

@Composable
fun DueTo(
    dayLabel: String,
    updateDate: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val date =
        remember { mutableStateOf("$year-${"%02d".format(month + 1)}-${"%02d".format(day)}") }

    val datePickerDialog = DatePickerDialog(
        context, { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            date.value = "$mYear-${"%02d".format(mMonth + 1)}-${"%02d".format(mDayOfMonth)}"
            updateDate(date.value)
        }, year, month, day
    )
    Button(
        elevation = null,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray),
        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 10.dp),
        modifier = Modifier.defaultMinSize(minHeight = 30.dp),
        onClick = { datePickerDialog.show() }) {
        Icon(
            Icons.Default.CalendarToday,
            contentDescription = stringResource(id = R.string.deadline),
            modifier = Modifier
                .height(14.dp)
                .padding(end = 10.dp)
        )
        Text(dayLabel)
    }


}