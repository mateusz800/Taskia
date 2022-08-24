@file:Suppress("unused")

package com.mabn.taskia.ui.calendar


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.model.KalendarType
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalDate

@Composable
fun CalendarDatePicker(onDateChange: (LocalDate) -> Unit, modifier: Modifier = Modifier) {
    val type = remember { mutableStateOf<KalendarType>(KalendarType.Oceanic) }
    Column(
        modifier = Modifier.offset(y = (-20).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandler(enabled = type.value == KalendarType.Firey) {
            type.value = KalendarType.Oceanic
        }
        Kalendar(
            kalendarType = type.value,
            kalendarThemeColor = KalendarThemeColor(
                backgroundColor = MaterialTheme.colors.background,
                dayBackgroundColor = MaterialTheme.colors.primary,
                headerTextColor = MaterialTheme.colors.onBackground
            ),
            kalendarDayColors = KalendarDayColors(
                textColor = MaterialTheme.colors.onBackground,
                selectedTextColor = MaterialTheme.colors.onPrimary
            ),
            onCurrentDayClick = { day, _ ->
                onDateChange(day.localDate.toJavaLocalDate())
            },
            modifier = modifier
        )
        //handle the dat-
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
        is KalendarType.Oceanic -> KalendarType.Firey
        is KalendarType.Firey -> KalendarType.Oceanic
    }
}

@Preview
@Composable
private fun CalendarDatePicker_Preview() {
    MaterialTheme {
        CalendarDatePicker(onDateChange = {})
    }
}