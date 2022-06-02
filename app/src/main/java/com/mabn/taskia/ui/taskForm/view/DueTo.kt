package com.mabn.taskia.ui.taskForm.view

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mabn.taskia.R
import com.mabn.taskia.ui.common.CustomTextField
import java.util.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun DueTo(
    dayLabel: String,
    updateDate: (String) -> Unit
) {
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
    Box(
        contentAlignment = Alignment.BottomStart,
    ) {
        ExposedDropdownMenuBox(
            expanded = menuExpanded.value,
            modifier = Modifier
                .padding(top = 10.dp)
                .width(200.dp),
            onExpandedChange = { menuExpanded.value = !menuExpanded.value }) {
            CustomTextField(
                leadingIcon = {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = stringResource(id = R.string.deadline),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .height(14.dp)
                    )
                },
                enabled = false,
                value = dayLabel,
                onValueChange = {},
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 10.dp)
                    //.border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                    .height(30.dp)
                    .widthIn(min=200.dp)
                    .focusable(false)


            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .padding(0.dp)
                    .focusable(true),
                expanded = menuExpanded.value,
                onDismissRequest = { menuExpanded.value = false }) {

                DropdownMenuItem(onClick = {
                    datePickerDialog.show()
                    menuExpanded.value = false
                }) {
                    Text(stringResource(id = R.string.pick_date))
                }
                Divider()
                DropdownMenuItem(onClick = {
                    date.value = ""
                    updateDate("")
                    menuExpanded.value = false
                }) {
                    Text(stringResource(id = R.string.no_deadline))
                }
            }
        }

    }

}