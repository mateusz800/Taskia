package com.mabn.taskia.ui.taskList.components.taskItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mabn.taskia.domain.util.dbConverter.LocalDateTimeConverter
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun TaskGeneralInfo(
    status: Boolean,
    title: String,
    dueDay: LocalDateTime? = null,
    startTime: String? = null,
    onCheck: () -> Boolean,
    onClick: (() -> Unit)?,
    expandStatus: Boolean = false,
    showDate: Boolean = false,
    expandFun: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                CustomCheckbox(status) {
                    onCheck()
                }
                Spacer(modifier = Modifier.width(5.dp))
                TitleLabel(title = title, isCompleted = status) {
                    onClick?.let { it() }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                if (startTime != null) {
                    TimeLabel(time = startTime)
                }
                if (expandFun != null) {
                    Spacer(Modifier.width(10.dp))
                    ExpandButton(toggle = expandFun, expand = expandStatus)
                }
            }
        }
        if (dueDay != null && showDate) {
            DueDayLabel(
                value = LocalDateTimeConverter.dateToString(
                    dueDay,
                    context = LocalContext.current
                ),
                color = if (dueDay.isBefore(
                        LocalDate.now().atStartOfDay()
                    )
                ) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
            )

        }
    }
}
