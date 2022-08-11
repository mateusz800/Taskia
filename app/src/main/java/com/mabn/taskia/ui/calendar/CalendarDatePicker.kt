package com.mabn.taskia.ui.calendar


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.KalendarStyle
import com.himanshoe.kalendar.common.data.KalendarEvent
import com.himanshoe.kalendar.ui.Kalendar
import com.himanshoe.kalendar.ui.KalendarType
import com.mabn.taskia.ui.taskList.components.TaskListSection
import java.time.LocalDate

@Composable
fun CalendarDatePicker(onDateChange: (LocalDate) -> Unit) {
    val type = remember { mutableStateOf<KalendarType>(KalendarType.Oceanic()) }
    val selectedDay = remember { mutableStateOf(LocalDate.now()) }
    Column(
        modifier = Modifier.offset(y = (-20).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandler(enabled = type.value == KalendarType.Firey()) {
            type.value = KalendarType.Oceanic()
        }
        Kalendar(kalendarType = type.value,
            kalendarStyle = KalendarStyle(
                kalendarSelector = KalendarSelector.Circle()
                    .copy(
                        selectedColor = MaterialTheme.colors.primary,
                        defaultColor = Color.Transparent,
                        defaultTextColor = MaterialTheme.colors.onBackground,
                        selectedTextColor = MaterialTheme.colors.onPrimary,
                        todayColor = MaterialTheme.colors.primary.copy(alpha = 0.3f)
                    ),
                kalendarBackgroundColor = Color.Transparent,
                kalendarColor = Color.Transparent,
                hasRadius = false,
                shape = RectangleShape
            ),
            onCurrentDayClick = { day, event ->
                onDateChange(day)
                //handle the date click listener
            }, errorMessage = {
                //Handle the error if any
            })
        /*
        IconButton(
            onClick = { type.value = toggleCalendar(type.value) },
            modifier = Modifier.offset(y = (-30).dp)
        ) {
            Icon(
                if (type.value == KalendarType.Oceanic()) Icons.Filled.ArrowDropDown
                else Icons.Filled.ArrowDropUp,
                null
            )
        }
         */
    }
}

private fun toggleCalendar(currentState: KalendarType): KalendarType {
    return when (currentState) {
        is KalendarType.Oceanic -> KalendarType.Firey()
        is KalendarType.Firey -> KalendarType.Oceanic()
    }
}

@Preview
@Composable
private fun CalendarDatePicker_Preview() {
    MaterialTheme {
        CalendarDatePicker(onDateChange = {})
    }
}