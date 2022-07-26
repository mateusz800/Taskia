package com.mabn.taskia.ui.taskForm.dateTime

import android.app.DatePickerDialog
import android.widget.DatePicker
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
fun TaskDate(updateDate: (String) -> Unit, dayLabel: String) {
    val context = LocalContext.current
    val menuExpanded = remember { mutableStateOf(false) }

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
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()

    Picker(label = dayLabel,
        icon = {
            Icon(
                Icons.Default.Timer,
                contentDescription = stringResource(id = R.string.deadline),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .height(14.dp)
            )
        }, menuItem = mapOf(Pair(stringResource(id = R.string.pick_date)) {
            datePickerDialog.show()
            menuExpanded.value = false
        }, Pair(stringResource(id = R.string.no_deadline)) {
            date.value = ""
            updateDate("")
            menuExpanded.value = false
        })
    )
}

@Preview
@Composable
private fun TaskDate_Preview() {
    MaterialTheme {
        Surface {
            TaskDate(updateDate = {}, dayLabel = "Today")
        }
    }
}
